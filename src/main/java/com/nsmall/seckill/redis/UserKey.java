package com.nsmall.seckill.redis;

/**
 * @ClassName UserKey
 * @Description 用户键前缀
 * @Author sky
 * @Date 19-5-28 上午9:42
 * @Version 1.0
 */

public class UserKey extends BasePrefix{

    public static final int TOKEN_EXPIRE = 3600*24*2;
    private UserKey( int expirSeconds,String prefix) {
        super(expirSeconds,prefix);
    }

//    public static UserKey getById = new UserKey("id");
//    public static UserKey getByName = new UserKey("name");
    public static UserKey token = new UserKey(TOKEN_EXPIRE,"tk");
}
