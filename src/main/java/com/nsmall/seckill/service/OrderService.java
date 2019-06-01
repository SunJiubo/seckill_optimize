package com.nsmall.seckill.service;

import com.nsmall.seckill.dao.OrderDAO;
import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.SeckillOrder;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.vo.GoodsVo;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName OrderService
 * @Description TODO
 * @Author Sky
 * @Date 2019/5/30 13:54
 * @Version 1.0
 **/
@Service
public class OrderService {
    @Autowired
    OrderDAO orderDAO;

    public OrderInfo creatOrder(User user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderDAO.insert(orderInfo);
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderId);
        seckillOrder.setUserId(user.getId());
        orderDAO.insertSeckillOrder(seckillOrder);
        return orderInfo;
    }

    public SeckillOrder getSeckillOrderByUserIdGoodsId(Long userId, long goodsId) {
        return orderDAO.getSeckillOrderByUserIdGoodsId(userId,goodsId);
    }

    public OrderInfo getSeckillOrderByUserIdOrderId(long userId, long orderId) {
        return orderDAO.getSeckillOrderByUserIdOrderId(userId,orderId);
    }

    public OrderInfo getSeckillOrderByUserId(Long userId) {
        return orderDAO.getSeckillOrderByUserId(userId);
    }
}
