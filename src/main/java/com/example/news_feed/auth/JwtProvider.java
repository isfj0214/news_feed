package com.example.news_feed.auth;

import com.example.news_feed.auth.repository.AccessTokenRepository;
import com.example.news_feed.auth.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    public static final long ACCESSTOKEN_TIME = 1000 * 60 * 30; // 30분
    public static final long REFRESHTOKEN_TIME = 1000 * 60 * 60 * 24 * 14; // 2주

    public static final String ACCESS_PREFIX_STRING = "Bearer ";
    public static final String ACCESS_HEADER_STRING = "Authorization";
    public static final String REFRESH_HEADER_STRING = "RefreshToken";

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public String createAccessToken(Long memberId, String name, Boolean isAdmin){

        jwtProperties.get

        Map<String, Object> claims = new HashMap<>();

        claims.put("memberId", memberId);
        claims.put("nickname", name);
        claims.put("isAdmin", Boolean.toString(isAdmin));

        Date expiration = new Date(System.currentTimeMillis() + ACCESSTOKEN_TIME);

        String accessToken = ACCESS_PREFIX_STRING + Jwts.builder()
                .setSubject(Long.toString(memberId))
                .addClaims(claims)
                .setExpiration(expiration)
                .signWith(this.getSigningKey())
                .compact();

//        accessTokenRepository.deleteAccessTokenByMemberId(memberId);
//        accessTokenRepository.addAccessToken(new AccessTokenDto(memberId, accessToken, expiration));

        return accessToken;
    }
}
