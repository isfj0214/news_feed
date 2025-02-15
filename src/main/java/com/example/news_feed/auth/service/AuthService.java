package com.example.news_feed.auth.service;

import com.example.news_feed.auth.JwtUtil;
import com.example.news_feed.auth.dto.LoginRequestDto;
import com.example.news_feed.auth.entity.RefreshToken;
import com.example.news_feed.auth.repository.RefreshTokenRepository;
import com.example.news_feed.auth.dto.JwtTokenDto;
import com.example.news_feed.common.encode.PasswordEncoder;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception401;
import com.example.news_feed.common.error.exception.Exception404;
import com.example.news_feed.common.error.exception.Exception409;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtTokenDto login(LoginRequestDto loginRequestDto){
        Member findMember = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND_BY_EMAIL));

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), findMember.getPassword())){
            throw new Exception401(ErrorCode.INVALID_PASSWORD);
        }

        if(refreshTokenRepository.existsByMember(findMember)){
            throw new Exception409(ErrorCode.ALREADY_LOGGED_IN);
        }

        String accessToken = jwtUtil.createAccessToken(findMember.getMemberId());
        String refreshToken = jwtUtil.createRefreshToken(findMember.getMemberId());

        refreshTokenRepository.save(RefreshToken.builder()
                .token(refreshToken)
                .member(findMember)
                .build());


        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(Long memberId){
        refreshTokenRepository.deleteByMemberId(memberId);
    }
}
