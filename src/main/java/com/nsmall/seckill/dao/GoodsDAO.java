package com.nsmall.seckill.dao;

import com.nsmall.seckill.domain.SeckillGoods;
import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface GoodsDAO {

    String TABLE_NAME_SKGOODS = " seckill.sk_goods_seckill ";
    String TABLE_NAME_SKGOODS_ANAME = " sg ";
    String TABLE_NAME_GOODS_ANAME = " g ";
    String TABLE_NAME_GOODS = " seckill.sk_goods ";
//    String INSERT_FIELDS = " nickname, password, salt,head,register_date,last_login_date,login_count ";
    String INSERT_FIELDS_SKGOODS = " sg.stock_count,sg.start_date,sg.end_date,sg.seckill_price ";
//    String SELECT_FIELDS = " id,"+INSERT_FIELDS;

    @Select({"select g.*, ",INSERT_FIELDS_SKGOODS, " from ",TABLE_NAME_SKGOODS,TABLE_NAME_SKGOODS_ANAME," left join ",
            TABLE_NAME_GOODS,TABLE_NAME_GOODS_ANAME," on sg.goods_id =g.id"})
    public List<GoodsVo> listGoodsVo();

    @Select({"select g.*, ",INSERT_FIELDS_SKGOODS, " from ",TABLE_NAME_SKGOODS,TABLE_NAME_SKGOODS_ANAME," left join ",
            TABLE_NAME_GOODS,TABLE_NAME_GOODS_ANAME," on sg.goods_id =g.id where g.id = #{goodsId}"})
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update({"update ",TABLE_NAME_SKGOODS," set stock_count=stock_count-1 where goods_id = #{goodsId}"})
    public int reduceStock(SeckillGoods g);
}
