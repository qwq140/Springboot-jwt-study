package com.cos.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.model.UserRepository;
import com.cos.jwt.util.CookieUtil;
import com.cos.jwt.util.JwtUtil;
import com.cos.jwt.util.RedisUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 시큐리티가 filter를 가지고 있는데 그 필터중에 BasicAuthenticationFilter 라는 것이 있다.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있다.
// 만약에 권한이나 인증이 필요한 주소가 아니라면 이 필터를 안탐.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private RedisUtil redisUtil;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, RedisUtil redisUtil) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.redisUtil = redisUtil;
    }

    //인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청이 됨.");

        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
        System.out.println("jwtHeader : " +jwtHeader);

        // header가 있는지 확인
        if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
        String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX,"");

        String username = null;
        String refreshJwt = null;
        String refreshUname = null;
        try {
            username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("username").asString();
            // 서명이 정상적으로 됨
            if(username != null){
                User userEntity = userRepository.findByUsername(username);
                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                // jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities()); // 강제로 authentication 객체 생성

                // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request,response);
            }
        } catch (SignatureVerificationException s){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "잘못된 토큰 서명입니다.");
            return ;
        } catch (TokenExpiredException t){
            Cookie refreshToken = CookieUtil.getCookie(request, JwtProperties.REFRESH_TOKEN_NAME);
            if(refreshToken!=null){
                refreshJwt = refreshToken.getValue();
            }
        }

        try {
            if(refreshJwt != null){
                refreshUname = redisUtil.getData(refreshJwt);
                username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(refreshJwt).getClaim("username").asString();
                if (refreshUname.equals(username)){
                    System.out.println("리프레시");
                    User userEntity = userRepository.findByUsername(refreshUname);
                    PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                    // jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities()); // 강제로 authentication 객체 생성

                    // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    String newJwtToken = JwtUtil.generateToken(userEntity);
                    response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+newJwtToken);
                    chain.doFilter(request,response);
                }
            }
        } catch (Exception e){

        }


    }
}
