package com.nsmall.seckill.service;

import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.SeckillOrder;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.redis.GoodsKey;
import com.nsmall.seckill.redis.RedisService;
import com.nsmall.seckill.redis.SeckillKey;
import com.nsmall.seckill.util.MD5Util;
import com.nsmall.seckill.util.UUIDUtil;
import com.nsmall.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

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

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goods) {
        //减库存
        boolean success = goodsService.reduceStock(goods);
        if(success){
            return orderService.creatOrder(user,goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver,""+goodsId,true);
    }

    private boolean getGoodsOver(Long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver,""+goodsId);
    }

    public long getSeckillResult(Long userId, long goodsId) {
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId,goodsId);
        if(order!=null){
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    public String creatSeckillPath(User user, long goodsId) {
        if(user==null||goodsId<=0){
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(SeckillKey.getSeckillPath,""+user.getId()+"_"+goodsId,str);
        return str;
    }

    public boolean checkPath(User user, long goodsId, String path) {
        if(user==null||path==null){
            return false;
        }
        String pathOld = redisService.get(SeckillKey.getSeckillPath,""+user.getId()+"_"+goodsId,String.class);
        return path.equals(pathOld);
    }

    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }

    public BufferedImage createVerifyCode(User user, long goodsId) {
        if(user==null||goodsId<=0){
            return null;
        }
        int width = 80;
        int height = 32;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for(int i = 0; i<50;i++){
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x,y,0,0);
        }

        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SeckillKey.getSeckillVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    private int calc(String exp) {
        try{
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};

    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 =  ops[rdm.nextInt(3)];
        char op2 =  ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerifyCode(User user, long goodsId, int verifyCode) {
        if(user==null||goodsId<=0){
            return false;
        }
        Integer codeOld = redisService.get(SeckillKey.getSeckillVerifyCode,user.getId()+","+goodsId,Integer.class);
        if(codeOld==null||codeOld - verifyCode !=0){
            return false;
        }
        redisService.delete(SeckillKey.getSeckillVerifyCode,user.getId()+","+goodsId);
        return true;
    }
}
