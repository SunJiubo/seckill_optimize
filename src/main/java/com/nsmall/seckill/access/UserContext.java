package com.nsmall.seckill.access;

import com.nsmall.seckill.domain.User;

/**
 * @ClassName UserContext
 * @Description TODO
 * @Author Sky
 * @Date 2019/6/5 22:26
 * @Version 1.0
 **/
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
        return userHolder.get();
    }
}
