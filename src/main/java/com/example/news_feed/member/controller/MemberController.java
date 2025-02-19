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
    public ResponseEntity<List<MemberResponseDto>> findAllMember(HttpServletRequest request)
    {
        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        return ResponseEntity.ok(memberService.findAllMember(memberId));
    }

    // 유저 단건 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> findByIdMember(
            @PathVariable Long memberId, HttpServletRequest request) {

        Long requestMemberId = Long.parseLong((String)request.getAttribute("memberId"));

        return ResponseEntity.ok(memberService.findByMemberId(memberId, requestMemberId));
    }

    // 프로필 수정(이름, 이메일 수정)
    @PatchMapping
    public ResponseEntity<MemberUpdateResponseDto> updateName(
            @RequestBody MemberUpdateRequestDto dto, HttpServletRequest request)

    {
        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        return ResponseEntity.ok(memberService.updateName(memberId, dto));
    }

    // 비밀번호 수정
    @PatchMapping("/password")
    public ResponseEntity<Map<String, String>> updatePassword(
            @RequestBody MemberUpdatePasswordRequestDto dto, HttpServletRequest request) {

        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        memberService.updatePassword(memberId, dto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "비밀번호가 성공적으로 변경되었습니다.");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public void delete(HttpServletRequest httpServletRequest) {
        Long memberId = Long.parseLong((String)httpServletRequest.getAttribute("memberId"));
        memberService.deleteByMemberId(memberId);
    }

}
