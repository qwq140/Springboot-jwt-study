package com.cos.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.jwt.JwtProperties;
import com.cos.jwt.model.User;

import java.util.Date;

public class JwtUtil {

    public static String generateToken(User user){
        return doGenerateToken(user, JwtProperties.ACCESS_TOKEN_EXPIRE_TIME);
    }

    public static String generateRefreshToken(User user){
        return doGenerateToken(user, JwtProperties.REFRESH_TOKEN_EXPIRE_TIME);
    }

    private static String doGenerateToken(User user, long expireTime){
        return JWT.create()
                .withSubject(JwtProperties.SECRET)
                .withExpiresAt(new Date(System.currentTimeMillis()+ expireTime)) // 토큰 만료시간
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

    }
}
