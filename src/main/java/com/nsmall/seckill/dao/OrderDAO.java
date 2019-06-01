package com.nsmall.seckill.dao;

import com.nsmall.seckill.domain.OrderInfo;
import com.nsmall.seckill.domain.SeckillOrder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * @ClassName OrderDAO
 * @Description TODO
 * @Author Sky
 * @Date 2019/5/30 15:00
 * @Version 1.0
 **/
@Mapper
@Component
public interface OrderDAO {

    String TABLE_NAME_SKORDER = " seckill.sk_order ";
    String INSERT_FIELDS_SKORDER = " user_id,order_id,goods_id ";
    String SELECT_FIELDS_SKORDER= " id,"+INSERT_FIELDS_SKORDER;

    String TABLE_NAME_ORDER_INFO = " seckill.sk_order_info ";
    String INSERT_FIELDS_ORDER_INFO = " user_id,goods_id,delivery_addr_id,goods_name,goods_count,goods_price,order_channel,status,create_date,pay_date ";
    String SELECT_FIELDS_ORDER_INFO = " id,"+INSERT_FIELDS_ORDER_INFO;

    @Insert({"insert into ",TABLE_NAME_ORDER_INFO,"(",INSERT_FIELDS_ORDER_INFO,
            ") values(#{userId},#{goodsId},#{deliveryAddrId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate},#{payDate})"})
    @SelectKey(keyColumn ="id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert({"insert into ",TABLE_NAME_SKORDER,"(",INSERT_FIELDS_SKORDER,
            ") values(#{userId},#{orderId},#{goodsId})"})
    public int insertSeckillOrder(SeckillOrder seckillOrder);

    @Select({"select ",SELECT_FIELDS_SKORDER," from",TABLE_NAME_SKORDER," where user_id=#{userId} and goods_id=#{goodsId}"})
    public SeckillOrder getSeckillOrderByUserIdGoodsId(@Param("userId") Long userId,
                                                       @Param("goodsId") long goodsId);

    @Select({"select ",SELECT_FIELDS_ORDER_INFO," from",TABLE_NAME_ORDER_INFO," where user_id=#{userId} and id=#{orderId}"})
    public OrderInfo getSeckillOrderByUserIdOrderId(@Param("userId")long userId,
                                                    @Param("orderId") long orderId);

    @Select({"select ",SELECT_FIELDS_ORDER_INFO," from",TABLE_NAME_ORDER_INFO," where user_id=#{userId}"})
    public OrderInfo getSeckillOrderByUserId(@Param("userId") Long userId);
}
