package com.example.news_feed.member.controller;

import com.example.news_feed.member.dto.request.MemberSaveRequestDto;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberSaveResponseDto> save(@Valid @RequestBody MemberSaveRequestDto requestDto){

        MemberSaveResponseDto memberSaveResponseDto = memberService.save(requestDto);
        return new ResponseEntity<>(memberSaveResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> findAllMember() {
        return ResponseEntity.ok(memberService.findAllMember());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> findByIdMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.findByIdMember(id));
    }

    @PatchMapping
    public ResponseEntity<MemberUpdateResponseDto> update(@RequestBody MemberUpdateRequestDto dto, HttpServletRequest httpServletRequest) {
        Long memberId = Long.parseLong((String)httpServletRequest.getAttribute("memberId"));
        MemberUpdateResponseDto updatedMember = memberService.update(memberId,dto);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping
    public void delete(HttpServletRequest httpServletRequest) {
        Long memberId = Long.parseLong((String)httpServletRequest.getAttribute("memberId"));
        memberService.deleteByIdMember(memberId);
    }

}
