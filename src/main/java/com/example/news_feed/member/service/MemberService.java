package com.example.news_feed.member.service;

import com.example.news_feed.common.encode.PasswordEncoder;
import com.example.news_feed.member.dto.MemberSaveRequestDto;
import com.example.news_feed.member.dto.MemberSaveResponseDto;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberSaveResponseDto save(MemberSaveRequestDto reqeustDto) {
        Member member = new Member(reqeustDto.getName(), reqeustDto.getEmail(), passwordEncoder.encode(reqeustDto.getPassword()));
        Member savedMember = memberRepository.save(member);
        return MemberSaveResponseDto.buildDto(savedMember);
    }
}
