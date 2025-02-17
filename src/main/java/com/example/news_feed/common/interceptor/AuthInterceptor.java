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
        else if(strs[1].equals("posts") && method.equals("GET")){
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        validateToken(request,  token);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private void validateToken(HttpServletRequest request, String token) {
        if(token != null){
            // 토큰 형식 올바른지 확인
            if(!(token.startsWith("access ") || token.startsWith("refresh "))){
                throw new Exception400(ErrorCode.JWT_FORMAT_ERROR);
            }

            Claims claims = jwtUtil.getClaims(token);
            request.setAttribute("memberId", claims.getSubject());

        }
        else{
            throw new Exception401(ErrorCode.TOKEN_NOT_PROVIDED);
        }
    }

}
