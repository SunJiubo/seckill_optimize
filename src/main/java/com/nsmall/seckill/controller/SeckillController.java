package com.nsmall.seckill.controller;

import com.nsmall.seckill.domain.Goods;
import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.SeckillOrder;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.redis.RedisService;
import com.nsmall.seckill.result.CodeMsg;
import com.nsmall.seckill.service.GoodsService;
import com.nsmall.seckill.service.OrderService;
import com.nsmall.seckill.service.SeckillService;
import com.nsmall.seckill.service.UserService;
import com.nsmall.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName SeckillController
 * @Description TODO 秒杀模块
 * @Author Sky
 * @Date 2019/5/30 13:45
 * @Version 1.0
 **/
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RequestMapping("/do_seckill")
    public String list(Model model, User user,
                       @RequestParam("goodsId") long goodsId){
        model.addAttribute("user",user);
        if(user==null){
            return "login";
        }

        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock<=0){
            model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
            return "seckill_fail";
        }
        //判断是否秒杀到了
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if(seckillOrder!=null){
            model.addAttribute("errmsg",CodeMsg.REPEATE_SECKILL.getMsg());
            return "seckill_fail";
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(user,goods);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);
        return "order_detail";
    }
}
