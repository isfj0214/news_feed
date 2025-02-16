package com.example.news_feed.auth;

import com.example.news_feed.auth.repository.RefreshTokenRepository;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception400;
import com.example.news_feed.common.error.exception.Exception401;
import com.example.news_feed.common.error.exception.RecreateAccessTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final long ACCESSTOKEN_TIME = 1000 * 60; //* 30; // 30분
    public static final long REFRESHTOKEN_TIME = 1000 * 60 * 2;//60 * 24 * 14; // 2주

    private final JwtProperties jwtProperties;

    public String createAccessToken(Long memberId){

        Date expiration = new Date(System.currentTimeMillis() + ACCESSTOKEN_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("access", "access");

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(Long.toString(memberId))
                .setExpiration(expiration)
                .signWith(getSigningKey())
                .compact();

        return accessToken;
    }

    public String createRefreshToken(Long memberId){

        Date expiration = new Date(System.currentTimeMillis() + REFRESHTOKEN_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("refresh", "refresh");

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(Long.toString(memberId))
                .setExpiration(expiration)
                .signWith(getSigningKey())
                .compact();

        return accessToken;
    }

    public Claims getAccessTokenClaims(String token){
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if(claims.get("access") == null){
                throw new Exception401(ErrorCode.ACCESS_TOKEN_REQUIRED);
            }
        } catch (ExpiredJwtException e) {

            if(e.getClaims().get("access") == null){
                throw new Exception401(ErrorCode.ACCESS_TOKEN_REQUIRED);
            }

            // 토큰이 만료되었습니다.
            throw new Exception401(ErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (JwtException e) {
            // 토큰 처리중 오류가 발생
            throw new Exception400(ErrorCode.JWT_ERROR);
        }

        return claims;
    }

    public Claims getRefreshTokenClaims(String token){
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if(claims.get("refresh") == null){
                throw new Exception401(ErrorCode.REFRESH_TOKEN_REQUIRED);
            }
        } catch (ExpiredJwtException e) {

            if(e.getClaims().get("refresh") == null){
                throw new Exception401(ErrorCode.REFRESH_TOKEN_REQUIRED);
            }

            throw e;
        } catch (JwtException e) {
            // 토큰 처리중 오류가 발생
            throw new Exception400(ErrorCode.JWT_ERROR);
        }

        return claims;
    }

    public Claims getClaims(String token){
        if(token.startsWith("access ")){
            // 엑세스 토큰이면 까서 만료되었으면 예외던져짐
            return getAccessTokenClaims(token.replace("access ", ""));
        }
        else{
            Claims claims = null;
            try{
                claims = getRefreshTokenClaims(token.replace("refresh ", ""));
                // refreshToken 이 만료되지 않았으므로 accessToken을 만들어서 보내줘야됨
                Long memberId = Long.parseLong(claims.getSubject());
                String accessToken = createAccessToken(memberId);
                throw new RecreateAccessTokenException(accessToken);
            }catch (ExpiredJwtException e){
                // 만료된 refresh토큰이면 db에서 지우고 예외를 claim에 담아서 인터셉터로 전달
                claims = e.getClaims();
                claims.put("error", new Exception401(ErrorCode.REFRESH_TOKEN_EXPIRED));
                return claims;
            }
        }
    }

    private SecretKey getSigningKey() {
        String secret = jwtProperties.getSecretKey(); // 32바이트 이상 필수
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
