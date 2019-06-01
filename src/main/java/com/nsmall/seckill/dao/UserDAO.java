package com.nsmall.seckill.dao;

import com.nsmall.seckill.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserDAO {

    String TABLE_NAME = " seckill.sk_user ";
    String INSERT_FIELDS = " nickname, password, salt,head,register_date,last_login_date,login_count ";
    String SELECT_FIELDS = " id,"+INSERT_FIELDS;

    @Select({"select ",SELECT_FIELDS, " from ",TABLE_NAME," where id=#{id}"})
    public User getById(@Param("id") long id);

}
