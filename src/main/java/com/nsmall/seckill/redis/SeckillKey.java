package com.nsmall.seckill.redis;

/**
 * @ClassName SeckillKey
 * @Description TODO
 * @Author Sky
 * @Date 2019/6/4 17:01
 * @Version 1.0
 **/
public class SeckillKey extends BasePrefix {



    public SeckillKey(int expirSeconds, String prefix) {
        super(expirSeconds, prefix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey(0,"go");
    public static SeckillKey getSeckillPath = new SeckillKey(60,"sp");
    public static SeckillKey getSeckillVerifyCode = new SeckillKey(300,"vc");

}
