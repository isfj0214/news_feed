package com.example.news_feed.auth.service;

import com.example.news_feed.auth.JwtUtil;
import com.example.news_feed.auth.dto.LoginRequestDto;
import com.example.news_feed.auth.repository.RefreshTokenRepository;
import com.example.news_feed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    public void login(LoginRequestDto loginRequestDto){

    }
}
