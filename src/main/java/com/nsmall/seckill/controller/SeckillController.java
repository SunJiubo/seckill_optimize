package com.nsmall.seckill.controller;

import com.nsmall.seckill.domain.Goods;
import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.SeckillOrder;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.redis.RedisService;
import com.nsmall.seckill.result.CodeMsg;
import com.nsmall.seckill.result.Result;
import com.nsmall.seckill.service.GoodsService;
import com.nsmall.seckill.service.OrderService;
import com.nsmall.seckill.service.SeckillService;
import com.nsmall.seckill.service.UserService;
import com.nsmall.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "/do_seckill",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> list(Model model, User user,
                       @RequestParam("goodsId") long goodsId){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock<=0){
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //判断是否秒杀到了
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if(seckillOrder!=null){
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(user,goods);
        return Result.success(orderInfo);
    }
}
