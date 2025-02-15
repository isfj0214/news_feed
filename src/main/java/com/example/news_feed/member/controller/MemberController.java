package com.example.news_feed.member.controller;

import com.example.news_feed.member.dto.MemberSaveRequestDto;
import com.example.news_feed.member.dto.MemberSaveResponseDto;
import com.example.news_feed.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping()
    public ResponseEntity<MemberSaveResponseDto> save(@RequestBody MemberSaveRequestDto reqeustDto){
        MemberSaveResponseDto memberSaveResponseDto = memberService.save(reqeustDto);
        return new ResponseEntity<>(memberSaveResponseDto, HttpStatus.CREATED);
    }

}
