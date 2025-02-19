package com.example.news_feed.common.interceptor;

import com.example.news_feed.auth.JwtUtil;
import com.example.news_feed.auth.service.AuthService;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception400;
import com.example.news_feed.common.error.exception.Exception401;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        String uri = request.getRequestURI();
        String method = request.getMethod();

        String[] strs = uri.split("/");

        if(strs[strs.length-1].equals("members") && method.equals("POST")){
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
        else if(strs[2].equals("posts") && method.equals("GET")){
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
        else if(strs[strs.length-1].equals("refresh")){
            validateRefreshToken(request, token);
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        validateAccessToken(request, token);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private void validateAccessToken(HttpServletRequest request, String token) {
        if(token != null){

            Claims claims = jwtUtil.getAccessTokenClaims(token);
            request.setAttribute("memberId", claims.getSubject());

        }
        else{
            throw new Exception401(ErrorCode.TOKEN_NOT_PROVIDED);
        }
    }

    private void validateRefreshToken(HttpServletRequest request, String token) {
        if(token != null){

            request.setAttribute("refreshToken", token);

        }
        else{
            throw new Exception401(ErrorCode.TOKEN_NOT_PROVIDED);
        }
    }

}
