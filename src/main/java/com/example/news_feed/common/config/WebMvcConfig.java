package com.example.news_feed.common.config;

import com.example.news_feed.auth.JwtUtil;
import com.example.news_feed.auth.service.AuthService;
import com.example.news_feed.common.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(jwtUtil))
                .addPathPatterns("/api/refresh")
                .addPathPatterns("/api/logout")
                .addPathPatterns("/api/members","/api/members/**")
                .addPathPatterns("/api/posts", "/api/posts/**")
                .addPathPatterns("/api/friends", "/api/friends/**")
                .order(1);
    }
}
