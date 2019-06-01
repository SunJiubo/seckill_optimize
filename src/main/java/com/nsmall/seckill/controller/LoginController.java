package com.nsmall.seckill.controller;

import com.nsmall.seckill.result.CodeMsg;
import com.nsmall.seckill.result.Result;
import com.nsmall.seckill.service.UserService;
import com.nsmall.seckill.util.ValidatorUtil;
import com.nsmall.seckill.vo.LoginVo;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author sky
 * @Date 19-5-28 下午4:48
 * @Version 1.0
 */

@Controller
@RequestMapping("/login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        logger.info(loginVo.toString());

        //参数校验
//        String passInput = loginVo.getPassword();
//        String mobile = loginVo.getMobile();
//
//        if(StringUtils.isEmpty(passInput)){
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if(StringUtils.isEmpty(mobile)){
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        if (!ValidatorUtil.isMobile(mobile)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
        //登录
//        CodeMsg cm =
        userService.login(response,loginVo);
        return Result.success(true);

    }


}
