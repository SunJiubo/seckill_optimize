package com.nsmall.seckill.rabbitmq;

import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.User;

/**
 * @ClassName SeckillMessage
 * @Description TODO 将秒杀的信息放入队列中，用于异步处理，创建订单
 * @Author Sky
 * @Date 2019/6/4 16:32
 * @Version 1.0
 **/
public class SeckillMessage {
    private User user;
    private long goodsId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
