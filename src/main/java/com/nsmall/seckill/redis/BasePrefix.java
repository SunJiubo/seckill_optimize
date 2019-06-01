package com.nsmall.seckill.redis;

/**
 * @ClassName BasePrefix
 * @Description TODO
 * @Author sky
 * @Date 19-5-28 上午9:29
 * @Version 1.0
 */

public abstract class BasePrefix implements KeyPrefix{

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix){
        this(0,prefix);//默认0，表示永不过期
    }

    public BasePrefix(int expirSeconds, String prefix) {
        this.expireSeconds = expirSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":"+prefix;
    }
}
