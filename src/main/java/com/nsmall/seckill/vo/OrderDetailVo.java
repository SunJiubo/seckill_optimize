package com.nsmall.seckill.vo;

import com.nsmall.seckill.domain.OrderInfo;

/**
 * @ClassName OrderDetailVo
 * @Description TODO 订单详情
 * @Author Sky
 * @Date 2019/6/2 19:03
 * @Version 1.0
 **/
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
