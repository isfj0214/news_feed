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

        if(strs[strs.length-1].equals("logout")){
            validateToken(request, token);
        }
        else if(strs[strs.length-1].equals("members") && (method.equals("PATCH") || method.equals("DELETE"))){
            // 유저 수정과 삭제
            validateToken(request, token);
        }
        else if(strs[1].equals("members") && method.equals("GET")){
            // 유저 단건 조회, 유저 전체 조회 -> 여기서 로그인한 사용자의 정보만 전부 보이고 다른 사용자의 민감한 정보는 감춰야됨
            validateToken(request, token);
        }
        else if(strs[strs.length-1].equals("posts") && (method.equals("POST") || method.equals("PATCH") || method.equals("DELETE"))){
            // 게시물 생성, 수정, 삭제
            validateToken(request, token);
        }
        else if(strs[1].equals("posts") && strs[strs.length-1].equals("likes") && (method.equals("POST") || method.equals("DELETE"))){
            // (게시물 좋아요, 취소), (게시물 댓글 좋아요, 취소)
            validateToken(request, token);
        }
        else if(strs[1].equals("posts") && strs[strs.length-1].equals("comments") && method.equals("POST")){
            // 게시물 댓글 생성
            validateToken(request, token);
        }
        else if(strs[1].equals("posts") && strs.length == 5 && (method.equals("PATCH") || method.equals("DELETE"))){
           // 게시물 댓글 수정, 삭제
            validateToken(request, token);
        }
        else if(strs[strs.length-1].equals("friends") && method.equals("GET")){
            // 친구 목록
            validateToken(request, token);
        }
        else if(strs[1].equals("friends") && strs[strs.length-1].equals("request") && (method.equals("GET") || method.equals("POST") || method.equals("DELETE"))){
            // 친구 요청 목록, 친구 요청, 친구 요청 취소
            validateToken(request, token);
        }
        else if(strs[1].equals("friends") && (strs[strs.length-1].equals("accept") || strs[strs.length-1].equals("reject")) && method.equals("POST")){
            // 친구 요청 수락, 친구 요청 거부
            validateToken(request, token);
        }
        else if(strs[1].equals("friends") && method.equals("DELETE")){
            // 친구 삭제
            validateToken(request, token);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private void validateToken(HttpServletRequest request, String token) {
        if(token != null){
            // 토큰 형식 올바른지 확인
            if(!(token.startsWith("access ") || token.startsWith("refresh "))){
                throw new Exception400(ErrorCode.JWT_FORMAT_ERROR);
            }

            Claims claims = jwtUtil.getClaims(token);
            if(claims.get("error") != null){
                throw (Exception401)claims.get("error");
            }
            request.setAttribute("memberId", claims.getSubject());

        }
        else{
            throw new Exception401(ErrorCode.TOKEN_NOT_PROVIDED);
        }
    }

}
