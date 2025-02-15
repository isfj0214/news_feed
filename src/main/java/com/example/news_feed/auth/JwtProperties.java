package com.example.news_feed.auth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource("classpath:application-jwt/properties")
public class JwtProperties {

    @Value("${jwt.seceret_key}")
    private String secretKey;

}
