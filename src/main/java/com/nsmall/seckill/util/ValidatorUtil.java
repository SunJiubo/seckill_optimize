package com.nsmall.seckill.util;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName ValidatorUtil
 * @Description TODO
 * @Author sky
 * @Date 19-5-28 下午6:50
 * @Version 1.0
 */

public class ValidatorUtil {
    public static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
        if(StringUtils.isEmpty(src)){
            return false;
        }
        Matcher m = mobile_pattern.matcher(src);
        return m.matches();
    }

}
