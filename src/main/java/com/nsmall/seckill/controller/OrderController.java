package com.nsmall.seckill.controller;

import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.redis.RedisService;
import com.nsmall.seckill.result.CodeMsg;
import com.nsmall.seckill.service.GoodsService;
import com.nsmall.seckill.service.OrderService;
import com.nsmall.seckill.service.UserService;
import com.nsmall.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName OrderController
 * @Description TODO
 * @Author Sky
 * @Date 2019/5/30 16:14
 * @Version 1.0
 **/
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail/{userId}/{orderId}")
    public String orderDetail(@PathVariable("userId")long userId,
                              @PathVariable("orderId")long orderId,
                              User user, Model model){
        if(user==null){
            return "login";
        }
        if(userId!=user.getId()){
            model.addAttribute("errmsg", CodeMsg.NOT_CURUSER.getMsg());
            return "order_error";
        }
        OrderInfo orderInfo = orderService.getSeckillOrderByUserIdOrderId(userId,orderId);
        model.addAttribute("orderInfo",orderInfo);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(orderInfo.getGoodsId());
        model.addAttribute("goods",goods);
        return "user_detail";
    }
}
