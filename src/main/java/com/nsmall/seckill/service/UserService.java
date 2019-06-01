package com.nsmall.seckill.service;

import com.nsmall.seckill.dao.UserDAO;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.exception.GlobalException;
import com.nsmall.seckill.redis.RedisService;
import com.nsmall.seckill.redis.UserKey;
import com.nsmall.seckill.result.CodeMsg;
import com.nsmall.seckill.util.MD5Util;
import com.nsmall.seckill.util.UUIDUtil;
import com.nsmall.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author sky
 * @Date 19-5-28 下午5:00
 * @Version 1.0
 */

@Service
public class UserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    UserDAO userDAO;

    @Autowired
    RedisService redisService;

    public User getById(long id){
        return userDAO.getById(id);
    }

    public User getByToken(HttpServletResponse response,String token){
        if(StringUtils.isEmpty(token)){
            return  null;
        }
        User user = redisService.get(UserKey.token,token,User.class);
        //延长有效期
        if(user!=null){
            addCookie(response,token,user);
        }
        return user;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo){
        if(loginVo==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formword = loginVo.getPassword();

        User user = userDAO.getById(Long.parseLong(mobile));
        if(user==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDbPass(formword,saltDB);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成token
        String token = UUIDUtil.uuid();
        //生成cookie
        addCookie(response,token,user);
        return true;
    }

    private void addCookie(HttpServletResponse response,String token,User user){

        //把私人信息缓存到第三方缓存缓存中
        redisService.set(UserKey.token,token,user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
