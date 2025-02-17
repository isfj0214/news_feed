package com.example.news_feed.member.service;

import com.example.news_feed.member.dto.request.MemberSaveRequestDto;
import com.example.news_feed.member.dto.request.MemberUpdateRequestDto;
import com.example.news_feed.member.dto.response.MemberResponseDto;
import com.example.news_feed.member.dto.response.MemberSaveResponseDto;
import com.example.news_feed.member.dto.response.MemberUpdateResponseDto;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberSaveResponseDto save(MemberSaveRequestDto requestDto) {

        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 email입니다.");
        }

        Member member = new Member(requestDto.getName(), requestDto.getEmail(), requestDto.getPassword());
        Member savedMember = memberRepository.save(member);
        return MemberSaveResponseDto.buildDto(savedMember);
    }

    public List<MemberResponseDto> findAllMember() {
        return memberRepository.findAll().stream()
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        member.getName(),
                        member.getEmail(),
                        member.getCreatedAt(),
                        member.getModifiedAt()))
                .collect(Collectors.toList());
    }

    public MemberResponseDto findByIdMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 id를 가진 회원을 찾을 수 없습니다.")
        );
        return new MemberResponseDto(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getCreatedAt(),
                member.getModifiedAt());
    }

    public MemberUpdateResponseDto update(Long id, MemberUpdateRequestDto dto) {
        Member member = memberRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 id를 가진 회원을 찾을 수 없습니다.")
        );
        member.update(dto.getName(),dto.getEmail());
        return new MemberUpdateResponseDto(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getCreatedAt(),
                member.getModifiedAt());
    }

    public void deleteByIdMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 id를 가진 회원을 찾을 수 없습니다.");
        }
        memberRepository.deleteById(id);
    }


}
