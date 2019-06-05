package com.nsmall.seckill.redis;

/**
 * @ClassName AccessKey
 * @Description TODO
 * @Author Sky
 * @Date 2019/6/5 22:33
 * @Version 1.0
 **/
public class AccessKey extends BasePrefix{

    private AccessKey( int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds){
        return new AccessKey(expireSeconds,"access");
    }
}
