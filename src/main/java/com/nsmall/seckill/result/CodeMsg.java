package com.nsmall.seckill.result;

public class CodeMsg {
    private int code;
    private String msg;


    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101,"参数校验异常:%s");
    public static final CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102,"请求非法");
    public static final CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500104, "访问太频繁！");

    //登录模块5002xx
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210,"Session不存在或已失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211,"登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212,"手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213,"手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214,"手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215,"密码错误");


    //商品模块5003xx

    //订单模块5004xx
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");

    //秒杀模块5005xx
    public static CodeMsg SECKILL_OVER = new CodeMsg(500500,"商品已秒杀完");
    public static CodeMsg REPEATE_SECKILL = new CodeMsg(500501,"不可重复秒杀");
    public static CodeMsg SECKILL_FAIL = new CodeMsg(500502,"秒杀失败");

    //用户模块5006xx
    public static CodeMsg NOT_CURUSER = new CodeMsg(500600,"无权查看他人订单");




    private CodeMsg(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public CodeMsg fillArgs(Object... args){
        int code = this.code;
        String message = String.format(this.msg,args);
        return new CodeMsg(code,message);
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

}
