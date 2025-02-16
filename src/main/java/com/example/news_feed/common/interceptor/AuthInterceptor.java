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

        if(token != null){

            // 토큰 형식 올바른지 확인
            if(!(token.startsWith("access ") || token.startsWith("refresh "))){
                throw new Exception400(ErrorCode.JWT_FORMAT_ERROR);
            }

            if(strs[strs.length-1].equals("logout")){
                Claims claims = jwtUtil.getClaims(token);
                if(claims.get("error") != null){
                    throw (Exception401)claims.get("error");
                }
                request.setAttribute("memberId", claims.getSubject());
            }
            else if(strs[strs.length-1].equals("members") && method.equals("PATCH") || method.equals("DELETE")){
                Claims claims = jwtUtil.getClaims(token);
                if(claims.get("error") != null){
                    throw (Exception401)claims.get("error");
                }
                request.setAttribute("memberId", claims.getSubject());
            }

        }
        else{
            throw new Exception401(ErrorCode.TOKEN_NOT_PROVIDED);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
