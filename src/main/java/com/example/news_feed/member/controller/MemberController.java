package com.example.news_feed.member.controller;

import com.example.news_feed.auth.JwtUtil;
import com.example.news_feed.member.dto.request.MemberSaveRequestDto;
import com.example.news_feed.member.dto.request.MemberUpdatePasswordRequestDto;
import com.example.news_feed.member.dto.request.MemberUpdateRequestDto;
import com.example.news_feed.member.dto.response.MemberResponseDto;
import com.example.news_feed.member.dto.response.MemberSaveResponseDto;
import com.example.news_feed.member.dto.response.MemberUpdateResponseDto;
import com.example.news_feed.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    // 유저 생성
    @PostMapping
    public ResponseEntity<MemberSaveResponseDto> create(@Valid @RequestBody MemberSaveRequestDto requestDto){

        MemberSaveResponseDto memberSaveResponseDto = memberService.create(requestDto);
        return new ResponseEntity<>(memberSaveResponseDto, HttpStatus.CREATED);
    }

    // 유저 전체 조회
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> findAllMember(
            @RequestHeader("Authorization")String token) {
        String extractedToken = token.replace("Bearer","");
        jwtUtil.getAccessTokenClaims(extractedToken); // 토큰 검증(로그인 된 사용자만 조회 가능)

        return ResponseEntity.ok(memberService.findAllMember());
    }

    // 유저 단건 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> findByIdMember(
            @PathVariable Long id,
            @RequestHeader ("Authorization") String token) {

        String extractedToken = token.replace("Bearer", "");
        Long requesterId = Long.parseLong(jwtUtil.getAccessTokenClaims(extractedToken).getSubject());

        return ResponseEntity.ok(memberService.findByIdMember(id, requesterId));
    }

    // 프로필 수정(이름, 이메일 수정)
    @PatchMapping
    public ResponseEntity<MemberUpdateResponseDto> update(
            @RequestBody MemberUpdateRequestDto dto,
            @RequestHeader("Authorization")String token) {

        String extractedToken = token.replace("Bearer","");
        Long memberId = Long.parseLong(jwtUtil.getAccessTokenClaims(extractedToken).getSubject());
        return ResponseEntity.ok(memberService.update(memberId, dto, memberId));
    }

    // 비밀번호 수정
    @PatchMapping("/password")
    public ResponseEntity<Map<String, String>> updatePassword(
            @RequestBody MemberUpdatePasswordRequestDto dto,
            @RequestHeader("Authorization")String token) {

        String extractedToken = token.replace("Bearer","");
        Long memberId = Long.parseLong(jwtUtil.getAccessTokenClaims(extractedToken).getSubject());

        memberService.updatePassword(memberId, dto, memberId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "비밀번호가 성공적으로 변경되었습니다.");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public void delete(HttpServletRequest httpServletRequest) {
        Long memberId = Long.parseLong((String)httpServletRequest.getAttribute("memberId"));
        memberService.deleteByIdMember(memberId);
    }

}
