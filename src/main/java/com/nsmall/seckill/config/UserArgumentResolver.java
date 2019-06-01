package com.nsmall.seckill.config;

import com.nsmall.seckill.domain.User;
import com.nsmall.seckill.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request,UserService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(paramToken)&&StringUtils.isEmpty(cookieToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return userService.getByToken(response,token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        if(cookies==null||cookies.length<=0){
            return null;
        }
        for (Cookie cookie :
                cookies) {
            if(cookie.getName().equals(cookieNameToken)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
