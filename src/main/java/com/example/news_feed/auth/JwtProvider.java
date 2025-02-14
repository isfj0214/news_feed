package com.example.news_feed.auth;

import com.example.news_feed.auth.repository.AccessTokenRepository;
import com.example.news_feed.auth.repository.RefreshTokenRepository;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class JwtProvider {
    public static final long ACCESSTOKEN_TIME = 1000 * 60 * 30; // 30분
    public static final long REFRESHTOKEN_TIME = 1000 * 60 * 60 * 24 * 14; // 2주

    private static String key;
    private static String keyBase64Encoded; // properties에 정의된 값
    private static SecretKey signingKey;

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public JwtProvider(@Value("${jwt.seceret_key}") String keyParam,
                       AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository) {
        key = keyParam;
        keyBase64Encoded = Base64.getEncoder().encodeToString(key.getBytes());
        signingKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;

    }
}
