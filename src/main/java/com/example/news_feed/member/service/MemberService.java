package com.example.news_feed.member.service;

import com.example.news_feed.common.encode.PasswordEncoder;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.*;
import com.example.news_feed.member.dto.request.MemberSaveRequestDto;
import com.example.news_feed.member.dto.request.MemberUpdatePasswordRequestDto;
import com.example.news_feed.member.dto.request.MemberUpdateRequestDto;
import com.example.news_feed.member.dto.response.MemberResponseDto;
import com.example.news_feed.member.dto.response.MemberSaveResponseDto;
import com.example.news_feed.member.dto.response.MemberUpdateResponseDto;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    private final EntityManager entityManager;

    //유저 생성
    @Transactional
    public MemberSaveResponseDto create(MemberSaveRequestDto requestDto) {

        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new Exception409(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Member member = new Member(requestDto.getName(),requestDto.getEmail(), passwordEncoder.encode(requestDto.getPassword()));

        Member savedMember = memberRepository.save(member);

        return MemberSaveResponseDto.buildDto(savedMember);
    }

    // 프로필 전체 조회 (Id랑 이름만 반환)
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAllMember() {
        return memberRepository.findAll().stream()
                .map(member -> new MemberResponseDto(
                        member.getMemberId(),
                        member.getName(),
                        null,
                        null,
                        null))
                .collect(Collectors.toList());
    }


    // 유저 프로필 조회
    @Transactional(readOnly = true)
    public MemberResponseDto findByIdMember(Long id, Long requesterId) {

        Member member = memberRepository.findById(id).orElseThrow(
                ()-> new Exception404(ErrorCode.MEMBER_NOT_FOUND)
        );

        // 타인일 시 Id와 이름 반환
        if (!requesterId.equals(member.getMemberId())) {
            return new MemberResponseDto(
                    member.getMemberId(),
                    member.getName(),
                    null,
                    null,
                    null);
        }
        // 본인일 시 전부 반환
        return new MemberResponseDto(
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getCreatedAt(),
                member.getModifiedAt());
    }

    // 프로필 정보 수정 (본인만 가능)
    @Transactional
    public MemberUpdateResponseDto update(
            Long memberId, MemberUpdateRequestDto dto, Long requesterId) {

        if (!memberId.equals(requesterId)) {
            throw new Exception403(ErrorCode.USER_ACCESS_DENIED);
        }

        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new Exception404(ErrorCode.MEMBER_NOT_FOUND)
        );

        member.update(dto.getName(),dto.getEmail());

        entityManager.flush();

        return new MemberUpdateResponseDto(
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getCreatedAt(),
                member.getModifiedAt());
    }

    public void updatePassword(Long memberId, MemberUpdatePasswordRequestDto dto, Long requesterId) {

        if (!memberId.equals(requesterId)) {
            throw new Exception403(ErrorCode.USER_ACCESS_DENIED);
        }

        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new Exception404(ErrorCode.MEMBER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            throw new Exception401(ErrorCode.INVALID_PASSWORD);
        }

        if (passwordEncoder.matches(dto.getNewPassword(), member.getPassword())) {
            throw new Exception400(ErrorCode.JWT_ERROR);
        }
        member.updatePassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    public void deleteByIdMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                ()-> new Exception404(ErrorCode.MEMBER_NOT_FOUND)
        );
        memberRepository.deleteById(id);
    }

}
