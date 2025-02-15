package com.example.news_feed.auth;

import com.example.news_feed.auth.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final long ACCESSTOKEN_TIME = 1000 * 60 * 30; // 30분
    public static final long REFRESHTOKEN_TIME = 1000 * 60 * 60 * 24 * 14; // 2주

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public String createAccessToken(Long memberId){

        Date expiration = new Date(System.currentTimeMillis() + ACCESSTOKEN_TIME);

        String accessToken = Jwts.builder()
                .setSubject(Long.toString(memberId))
                .setExpiration(expiration)
                .signWith(getSigningKey())
                .compact();

        return accessToken;
    }

    public String createRefreshToken(Long memberId){

        Date expiration = new Date(System.currentTimeMillis() + REFRESHTOKEN_TIME);

        String accessToken = Jwts.builder()
                .setSubject(Long.toString(memberId))
                .setExpiration(expiration)
                .signWith(getSigningKey())
                .compact();

        return accessToken;
    }

    public boolean isTokenValid(String refreshToken){
        return false;
    }

    private Claims getClaims(String token){
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("JWT 토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            System.out.println("JWT 형식이 올바르지 않습니다.");
        } catch (SignatureException e) {
            System.out.println("JWT 서명이 올바르지 않습니다.");
        } catch (Exception e) {
            System.out.println("JWT 검증 중 오류 발생.");
        }

        return claims;
    }

    private SecretKey getSigningKey() {
        String secret = jwtProperties.getSecretKey(); // 32바이트 이상 필수
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
