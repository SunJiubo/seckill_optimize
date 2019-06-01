package com.nsmall.seckill.service;

import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName SeckillService
 * @Description TODO
 * @Author Sky
 * @Date 2019/5/30 13:58
 * @Version 1.0
 **/
@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goods) {
        goodsService.reduceStock(goods);

        return orderService.creatOrder(user,goods);
    }
}
