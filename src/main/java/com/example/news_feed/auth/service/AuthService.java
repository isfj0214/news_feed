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

        String accessToken = jwtUtil.createAccessToken(findMember.getId());
        String refreshToken = jwtUtil.createRefreshToken(findMember.getId());

        RefreshToken findRefreshToken = refreshTokenRepository.findByMember(findMember).orElse(null);
        if(findRefreshToken != null){
            try{
                jwtUtil.getRefreshTokenClaims(findRefreshToken.getToken());
                // 만약 db에 저장된 토큰이 만료되지 않았으면 이미 로그인 되어있다고 예외 던짐
                throw new Exception409(ErrorCode.ALREADY_LOGGED_IN);
            }catch (Exception401 e){
                // db에 저장된 토큰이 만료되었으면 해당 토큰을 삭제
                refreshTokenRepository.deleteByMemberId(findMember.getId());
            }
        }

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

    @Transactional
    public JwtTokenDto refreshToken(String requestRefreshToken){

        // 요청받은 토큰이 refresh 토큰인지, 만료된 토큰인지 검사
        Claims claims = jwtUtil.getRefreshTokenClaims(requestRefreshToken);
        String memberId = (String)claims.getSubject();

        // refresh 토큰을 db에서 찾아본다. 찾지 못한다면 이는 로그아웃 된 것
        RefreshToken findRefreshToken = refreshTokenRepository.findByToken(requestRefreshToken).orElseThrow(() -> new Exception401(ErrorCode.REFRESH_TOKEN_EXPIRED));

        String accessToken = jwtUtil.createAccessToken(Long.parseLong(memberId));
        String refreshToken = jwtUtil.createRefreshToken(Long.parseLong(memberId));

        findRefreshToken.updateToken(refreshToken);

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
