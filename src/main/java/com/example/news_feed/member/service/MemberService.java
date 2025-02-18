package com.example.news_feed.member.service;

import com.example.news_feed.common.encode.PasswordEncoder;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception404;
import com.example.news_feed.common.error.exception.Exception409;
import com.example.news_feed.member.dto.request.MemberSaveRequestDto;
import com.example.news_feed.member.dto.request.MemberUpdateRequestDto;
import com.example.news_feed.member.dto.response.MemberResponseDto;
import com.example.news_feed.member.dto.response.MemberSaveResponseDto;
import com.example.news_feed.member.dto.response.MemberUpdateResponseDto;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberSaveResponseDto save(MemberSaveRequestDto requestDto) {

        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new Exception409(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Member member = new Member(requestDto.getName(), requestDto.getEmail(), passwordEncoder.encode(requestDto.getPassword()));

        Member savedMember = memberRepository.save(member);

        return MemberSaveResponseDto.buildDto(savedMember);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAllMember() {
        return memberRepository.findAll().stream()
                .map(member -> new MemberResponseDto(
                        member.getMemberId(),
                        member.getName(),
                        member.getEmail(),
                        member.getCreatedAt(),
                        member.getModifiedAt()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findByIdMember(Long id) {

        Member member = memberRepository.findById(id).orElseThrow(
                ()-> new Exception404(ErrorCode.MEMBER_NOT_FOUND)
        );

        return new MemberResponseDto(
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getCreatedAt(),
                member.getModifiedAt());
    }

    @Transactional
    public MemberUpdateResponseDto update(Long id, MemberUpdateRequestDto dto) {
        Member member = memberRepository.findById(id).orElseThrow(
                ()-> new Exception404(ErrorCode.MEMBER_NOT_FOUND)
        );

        if (!member.getPassword().equals(dto.getPassword())) {
            throw new Exception404(ErrorCode.MEMBER_NOT_FOUND);
        }

        member.update(dto.getName(),dto.getEmail());
        return new MemberUpdateResponseDto(
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getCreatedAt(),
                member.getModifiedAt());
    }

    public void deleteByIdMember(Long id) {
        memberRepository.deleteById(id);
    }
    
}
