package com.example.news_feed.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-jwt/properties")
public class JwtProperties {

    @Value("${jwt.seceret_key}")
    private String secretKey;

}
