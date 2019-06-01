package com.nsmall.seckill.redis;

/**
 * @ClassName KeyPrefix
 * @Description 缓冲key前缀
 * @Author sky
 * @Date 19-5-28 上午9:11
 * @Version 1.0
 */

public interface KeyPrefix {

    /**
     * 有效期
     * @return
     */
    public int expireSeconds();


    /**
     * 前缀
     * @return
     */
    public String getPrefix();
}
