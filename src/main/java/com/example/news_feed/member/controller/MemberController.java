package com.example.news_feed.member.controller;

import com.example.news_feed.member.dto.request.MemberSaveRequestDto;
import com.example.news_feed.member.dto.request.MemberUpdateRequestDto;
import com.example.news_feed.member.dto.response.MemberResponseDto;
import com.example.news_feed.member.dto.response.MemberSaveResponseDto;
import com.example.news_feed.member.dto.response.MemberUpdateResponseDto;
import com.example.news_feed.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<MemberSaveResponseDto> save(@Valid @RequestBody MemberSaveRequestDto reqeustDto){
        MemberSaveResponseDto memberSaveResponseDto = memberService.save(reqeustDto);
        return new ResponseEntity<>(memberSaveResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> findAllMember() {
        return ResponseEntity.ok(memberService.findAllMember());
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponseDto> findByIdMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.findByIdMember(id));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberUpdateResponseDto> update(
            @PathVariable Long id,
            @RequestBody MemberUpdateRequestDto dto) {
        MemberUpdateResponseDto updatedMember = memberService.update(id,dto);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/members/{id}")
    public void delete(@PathVariable Long id) {
        memberService.deleteByIdMember(id);
    }

}
