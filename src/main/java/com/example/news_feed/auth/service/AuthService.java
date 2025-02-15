package com.example.news_feed.auth.service;

import com.example.news_feed.auth.JwtUtil;
import com.example.news_feed.auth.dto.LoginRequestDto;
import com.example.news_feed.auth.repository.RefreshTokenRepository;
import com.example.news_feed.common.encode.PasswordEncoder;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception401;
import com.example.news_feed.common.error.exception.Exception404;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void login(LoginRequestDto loginRequestDto){
        Member findMember = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND_BY_EMAIL));
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), findMember.getPassword())){
            throw new Exception401(ErrorCode.INVALID_PASSWORD);
        }
    }
}
