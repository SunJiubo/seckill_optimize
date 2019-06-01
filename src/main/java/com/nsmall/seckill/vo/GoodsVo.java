package com.nsmall.seckill.vo;

import com.nsmall.seckill.domain.Goods;

import java.util.Date;

/**
 * @ClassName GoodsVo
 * @Description TODO
 * @Author Sky
 * @Date 2019/5/30 9:48
 * @Version 1.0
 **/
public class GoodsVo extends Goods {
    private Integer stockCount;//秒杀库存
    private Date startDate;//开始时间
    private Date endDate;//结束时间
    private Double seckillPrice;//秒杀价格

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(Double seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    @Override
    public String toString() {
        return "GoodsVo{" +"goodsName"+getGoodsName()+
                "stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", seckillPrice=" + seckillPrice +
                '}';
    }
}
