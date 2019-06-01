package com.nsmall.seckill.controller;

import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.redis.RedisService;
import com.nsmall.seckill.result.Result;
import com.nsmall.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Sky
 * @Date 2019/5/31 10:07
 * @Version 1.0
 **/

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<User> info(Model model,User user){
        return Result.success(user);
    }
}
