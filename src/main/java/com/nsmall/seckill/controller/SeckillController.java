package com.nsmall.seckill.controller;

import com.nsmall.seckill.access.AccessLimit;
import com.nsmall.seckill.domain.Goods;
import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.SeckillOrder;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.rabbitmq.MQSender;
import com.nsmall.seckill.rabbitmq.SeckillMessage;
import com.nsmall.seckill.redis.GoodsKey;
import com.nsmall.seckill.redis.OrderKey;
import com.nsmall.seckill.redis.RedisService;
import com.nsmall.seckill.redis.SeckillKey;
import com.nsmall.seckill.result.CodeMsg;
import com.nsmall.seckill.result.Result;
import com.nsmall.seckill.service.GoodsService;
import com.nsmall.seckill.service.OrderService;
import com.nsmall.seckill.service.SeckillService;
import com.nsmall.seckill.service.UserService;
import com.nsmall.seckill.vo.GoodsVo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SeckillController
 * @Description TODO 秒杀模块
 * @Author Sky
 * @Date 2019/5/30 13:45
 * @Version 1.0
 **/
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(SeckillController.class);

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

    @Autowired
    MQSender sender;


    //标记，判断该商品是不是被处理过了
    private  Map<Long, Boolean> localOverMap = new HashMap<>();

    /**
     *      * GET POST
     *      * 1、GET幂等,服务端获取数据，无论调用多少次结果都一样
     *      * 2、POST，向服务端提交数据，不是幂等
     *      * <p>
     *      * 将同步下单改为异步下单
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/{path}/do_seckill",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, User user,
                                @RequestParam("goodsId") long goodsId,
                                @PathVariable("path") String path){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user",user);
        //验证path
        boolean check = seckillService.checkPath(user,goodsId,path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        //预减少库存，如果redis中的库存为0了，说明mysql中也没有了
        long stock = redisService.decr(GoodsKey.getGoodsStock,""+goodsId);
        if(stock<0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //判断是否秒杀到了
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if(seckillOrder!=null){
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        //入队
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setUser(user);
        seckillMessage.setGoodsId(goodsId);
        sender.sendSeckillMessage(seckillMessage);

        return Result.success(0);
    }
    //系统初始化时，将库存数据从mysql中取出放入redis，并将内存中的标记位设置为false
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList==null){
            return;
        }
        for(GoodsVo goods: goodsList){
            redisService.set(GoodsKey.getGoodsStock,""+goods.getId(),goods.getStockCount());
            localOverMap.put(goods.getId(),false);
        }
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, User user,
                                      @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user",user);
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long orderId = seckillService.getSeckillResult(user.getId(),goodsId);
        return Result.success(orderId);
    }

    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillPath(HttpServletRequest request, User user,
                                         @RequestParam("goodsId") long goodsId,
                                         @RequestParam(value = "verifyCode",defaultValue = "0") int verifyCode) {
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = seckillService.checkVerifyCode(user,goodsId,verifyCode);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = seckillService.creatSeckillPath(user,goodsId);
//        logger.info("路径:"+path);
        return Result.success(path);
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillVerifyCod(HttpServletResponse response, User user,
                                              @RequestParam("goodsId") long goodsId) {
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = seckillService.createVerifyCode(user,goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.SECKILL_FAIL);
        }
        String path = seckillService.creatSeckillPath(user,goodsId);
        logger.info("路径:"+path);
        return Result.success(path);
    }

    @RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for(GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getGoodsStock, ""+goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getSeckillOrderByUidGid);
        redisService.delete(SeckillKey.isGoodsOver);
        seckillService.reset(goodsList);
        return Result.success(true);
    }
}
