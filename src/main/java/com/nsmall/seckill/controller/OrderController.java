package com.nsmall.seckill.controller;

import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.redis.RedisService;
import com.nsmall.seckill.result.CodeMsg;
import com.nsmall.seckill.result.Result;
import com.nsmall.seckill.service.GoodsService;
import com.nsmall.seckill.service.OrderService;
import com.nsmall.seckill.service.UserService;
import com.nsmall.seckill.vo.GoodsDetailVo;
import com.nsmall.seckill.vo.GoodsVo;
import com.nsmall.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

//    @RequestMapping("/detail/{userId}/{orderId}")
//    public String orderDetail(@PathVariable("userId")long userId,
//                              @PathVariable("orderId")long orderId,
//                              User user, Model model){
//        if(user==null){
//            return "login";
//        }
//        if(userId!=user.getId()){
//            model.addAttribute("errmsg", CodeMsg.NOT_CURUSER.getMsg());
//            return "order_error";
//        }
//        OrderInfo orderInfo = orderService.getSeckillOrderByUserIdOrderId(userId,orderId);
//        model.addAttribute("orderInfo",orderInfo);
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(orderInfo.getGoodsId());
//        model.addAttribute("goods",goods);
//        return "user_detail";
//    }

    @RequestMapping("detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model,
                                      User user,
                                      @RequestParam("orderId") long orderId){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getSeckillOrderById(orderId);
        if(order == null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setGoods(goods);
        vo.setOrder(order);

        return Result.success(vo);
    }
}
