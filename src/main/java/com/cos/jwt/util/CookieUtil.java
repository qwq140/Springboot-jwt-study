package com.cos.jwt.util;

import com.cos.jwt.config.jwt.JwtProperties;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

    public static Cookie createCookie(String cookieName, String value){
        Cookie token = new Cookie(cookieName, value);
        token.setHttpOnly(true); // 자바스크립트로 쿠키를 조회하는 것을 막는 옵션
        token.setMaxAge((int) JwtProperties.REFRESH_TOKEN_EXPIRE_TIME);
        token.setPath("/"); // 모든 URL 범위에서 전송
        return token;
    }

    public static Cookie getCookie(HttpServletRequest req, String cookieName){
        final Cookie[] cookies = req.getCookies();
        if(cookies==null)
            return null;
        for(Cookie cookie : cookies){
            if (cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;

    }
}
