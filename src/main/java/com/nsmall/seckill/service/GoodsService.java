package com.nsmall.seckill.service;

import com.nsmall.seckill.dao.GoodsDAO;
import com.nsmall.seckill.domain.SeckillGoods;
import com.nsmall.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName GoodsService
 * @Description TODO
 * @Author Sky
 * @Date 2019/5/30 10:01
 * @Version 1.0
 **/
@Service
public class GoodsService {
    @Autowired
    GoodsDAO goodsDAO;

    public List<GoodsVo> listGoodsVo(){
        return goodsDAO.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId){
        return goodsDAO.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goods.getId());
        int ret = goodsDAO.reduceStock(g);
        return ret>0;
    }

    public void resetStock(List<GoodsVo> goodsList) {
        for(GoodsVo goods : goodsList ) {
            SeckillGoods g = new SeckillGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsDAO.resetStock(g);
        }
    }
}
