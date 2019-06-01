package com.nsmall.seckill.controller;

import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.redis.RedisService;
import com.nsmall.seckill.service.GoodsService;
import com.nsmall.seckill.service.OrderService;
import com.nsmall.seckill.service.UserService;
import com.nsmall.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    /**
     * 1274
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/to_list")
    public String List(Model model,User user){

        List<GoodsVo> goodsVos = goodsService.listGoodsVo();

//        OrderInfo orderInfo = orderService.getSeckillOrderByUserId(user.getId());

        model.addAttribute("user",user);
        model.addAttribute("goodsVos",goodsVos);
//        model.addAttribute("orderId",orderInfo.getId());

        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, User user,
                         @PathVariable("goodsId")long goodsId){

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("user",user);
        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int sk_status = 0;
        int remainSeconds = 0;

        if(now<startAt){//秒杀未开始，进行倒计时
            sk_status=0;
            remainSeconds = (int) ((startAt-now)/1000);
        }else if(now>endAt){
            //秒杀结束了
            sk_status = 2;
            remainSeconds=-1;
        }else {
            //秒杀进行中
            sk_status = 1;
            remainSeconds=0;
        }

        model.addAttribute("sk_status",sk_status);
        model.addAttribute("remainSeconds",remainSeconds);

        return "goods_detail";
    }
}
