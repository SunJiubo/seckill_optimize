package com.nsmall.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @ClassName MD5Util
 * @Description TODO
 * @Author sky
 * @Date 19-5-28 下午3:25
 * @Version 1.0
 */

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";


    public static String inputPassToFormPass(String inputPass){
        String str = "" + salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDbPass(String formPass,String salt){
        String str = "" + salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass,String saltDB){
        String formPass = inputPassToFormPass(inputPass);
        String DbPass = formPassToDbPass(formPass,saltDB);
        return DbPass;
    }


//    public static void main(String[] args) {
//        System.out.println(inputPassToFormPass("123456"));
//        System.out.println(formPassToDbPass(inputPassToFormPass("123456"),"1a2b3c4d"));
//        System.out.println(inputPassToDbPass("123456","1a2b3c4d"));
//    }
}
