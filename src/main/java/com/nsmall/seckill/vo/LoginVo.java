package com.nsmall.seckill.vo;

import com.nsmall.seckill.validator.IsMobile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @ClassName LoginVo
 * @Description TODO
 * @Author sky
 * @Date 19-5-28 下午4:58
 * @Version 1.0
 */

public class LoginVo {

    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
