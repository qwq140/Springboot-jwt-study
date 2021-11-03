package com.cos.jwt.config.jwt;

public interface JwtProperties {
    String SECRET = "cos";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String REFRESH_TOKEN_NAME = "refreshToken";

    long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
}
