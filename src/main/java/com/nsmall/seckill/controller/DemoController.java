package com.nsmall.seckill.controller;

import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.redis.UserKey;
import com.nsmall.seckill.result.CodeMsg;
import com.nsmall.seckill.result.Result;
import com.nsmall.seckill.redis.RedisService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    RedisService redisService;


//    @Autowired
//    MQSender sender;

//    @RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> mq() {
//        sender.send("hello,imooc");
//        return Result.success("Hello，world");
//    }

//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> topic() {
//        sender.sendTopic("hello,imooc");
//        return Result.success("Hello，world");
//    }
//    @Autowired
//    UserService userService;
//
    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
        return Result.success("hello, Jesper");
    }

    @RequestMapping("/Error")
    @ResponseBody
    public Result<String> error() {
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/Thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "Jesper");
        return "hello";
    }

//    @RequestMapping("/redis/get")
//    @ResponseBody
//    public Result<User> redisGet() {
//        User user = redisService.get(UserKey.getById,""+1,User.class);
//        return Result.success(user);
//    }
//
//    @RequestMapping("/redis/set")
//    @ResponseBody
//    public Result<Boolean> redisSet() {
//        User user = new User();
//        user.setId(1l);
//        user.setNickname("Jesper");
//        redisService.set(UserKey.getById, ""+1,user);
//        return Result.success(true);
//    }
//
//    @RequestMapping("/db/doubleInsert")
//    @ResponseBody
//    public Result<Boolean> doubleInsert() {
//        try {
//            userService.doubleInsert();
//            return Result.success(true);
//        } catch (Exception e) {
//            return Result.error(CodeMsg.PRIMARY_ERROR);
//        }
//    }
//
//    @RequestMapping("/db/get")
//    @ResponseBody
//    public Result<User> dbGet() {
//        User user = userService.getById(1);
//        return Result.success(user);
//    }

}
