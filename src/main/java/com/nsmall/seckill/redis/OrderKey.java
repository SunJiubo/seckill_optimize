package com.nsmall.seckill.redis;

/**
 * @ClassName OrderKey
 * @Description TODO
 * @Author Sky
 * @Date 2019/6/2 19:46
 * @Version 1.0
 **/
public class OrderKey extends BasePrefix{
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getSeckillOrderByUidGid = new OrderKey("seckill");
}
