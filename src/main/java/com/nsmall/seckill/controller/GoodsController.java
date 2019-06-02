package com.nsmall.seckill.controller;

import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.redis.GoodsKey;
import com.nsmall.seckill.redis.RedisService;
import com.nsmall.seckill.result.Result;
import com.nsmall.seckill.service.GoodsService;
import com.nsmall.seckill.service.OrderService;
import com.nsmall.seckill.service.UserService;
import com.nsmall.seckill.vo.GoodsDetailVo;
import com.nsmall.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * 1274
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String List(HttpServletRequest request,
                       HttpServletResponse response,
                       Model model, User user){


//        OrderInfo orderInfo = orderService.getSeckillOrderByUserId(user.getId());

        model.addAttribute("user",user);
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsVos",goodsVos);
//        model.addAttribute("orderId",orderInfo.getId());

//        return "goods_list";
        WebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model, User user,
                         @PathVariable("goodsId")long goodsId){

        model.addAttribute("user",user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
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

//        return "goods_detail";

        WebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Model model, User user,
                                        @PathVariable("goodsId")long goodsId){

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
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

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setSeckillStatus(sk_status);

        return Result.success(vo);
    }
}
