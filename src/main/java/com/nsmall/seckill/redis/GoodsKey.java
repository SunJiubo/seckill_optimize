package com.nsmall.seckill.redis;

/**
 * @ClassName GoodsKey
 * @Description TODO
 * @Author Sky
 * @Date 2019/6/1 9:32
 * @Version 1.0
 **/
public class GoodsKey extends BasePrefix {
    private GoodsKey(int expireSeconds, String prefix){
        super(expireSeconds,prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");

}
