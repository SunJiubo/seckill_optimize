package com.nsmall.seckill.service;

import com.nsmall.seckill.dao.OrderDAO;
import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.SeckillOrder;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.redis.OrderKey;
import com.nsmall.seckill.redis.RedisService;
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

    @Autowired
    RedisService redisService;

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
        orderDAO.insert(orderInfo);
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        orderDAO.insertSeckillOrder(seckillOrder);

        redisService.set(OrderKey.getSeckillOrderByUidGid,"" + user.getId() + "_" + goods.getId(),seckillOrder);
        return orderInfo;
    }

    public SeckillOrder getSeckillOrderByUserIdGoodsId(Long userId, long goodsId) {
//        return orderDAO.getSeckillOrderByUserIdGoodsId(userId,goodsId);
        return redisService.get(OrderKey.getSeckillOrderByUidGid,"" + userId + "_" + goodsId,SeckillOrder.class);
    }

    public OrderInfo getSeckillOrderByUserIdOrderId(long userId, long orderId) {
        return orderDAO.getSeckillOrderByUserIdOrderId(userId,orderId);
    }

    public OrderInfo getSeckillOrderByUserId(Long userId) {
        return orderDAO.getSeckillOrderByUserId(userId);
    }

    public OrderInfo getSeckillOrderById(long orderId) {
        return orderDAO.getSeckillOrderById(orderId);
    }

    public void deleteOrders() {
        orderDAO.deleteOrders();
        orderDAO.deleteSeckillOrders();
    }
}
