package com.example.news_feed.member.service;

import com.example.news_feed.auth.repository.RefreshTokenRepository;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.comment.repository.LikeCommentRepository;
import com.example.news_feed.common.encode.PasswordEncoder;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.*;
import com.example.news_feed.friend.repository.FriendRepository;
import com.example.news_feed.member.dto.request.MemberSaveRequestDto;
import com.example.news_feed.member.dto.request.MemberUpdatePasswordRequestDto;
import com.example.news_feed.member.dto.request.MemberUpdateRequestDto;
import com.example.news_feed.member.dto.response.*;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import com.example.news_feed.post.repository.PostLikeRepository;
import com.example.news_feed.post.repository.PostRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;

    private final PostLikeRepository postLikeRepository;
    private final FriendRepository friendRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

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

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAllMember(Long memberId) {
        return memberRepository.findAll().stream()
                .map(member -> member.getId() == memberId ? new MemberPrivateResponseDto(
                        member.getId(), member.getName(), member.getEmail(), member.getCreatedAt(), member.getModifiedAt())
                        : new MemberPublicResponseDto(member.getName(), member.getEmail()))
                .collect(Collectors.toList());
    }


    // 유저 프로필 조회
    @Transactional(readOnly = true)
    public MemberResponseDto findByMemberId(Long memberId, Long requestMemberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new Exception404(ErrorCode.MEMBER_NOT_FOUND)
        );

        // 타인일 시 Id와 이름 반환
        if (!requestMemberId.equals(member.getId())) {
            return new MemberPublicResponseDto(
                    member.getName(),
                    member.getEmail());
        }
        // 본인일 시 전부 반환
        return new MemberPrivateResponseDto(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getCreatedAt(),
                member.getModifiedAt());
    }

    // 프로필 정보 수정 (본인만 가능)
    public MemberUpdateResponseDto updateName(
            Long memberId, MemberUpdateRequestDto dto) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new Exception404(ErrorCode.MEMBER_NOT_FOUND)
        );

        member.update(dto.getName());
        memberRepository.save(member);

        return new MemberUpdateResponseDto(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getCreatedAt(),
                member.getModifiedAt());
    }

    @Transactional
    public void updatePassword(Long memberId, MemberUpdatePasswordRequestDto dto) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new Exception404(ErrorCode.MEMBER_NOT_FOUND)
        );

        if (passwordEncoder.matches(dto.getNewPassword(), member.getPassword())) {
            throw new Exception409(ErrorCode.USING_PASSWORD);
        }
        member.updatePassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    @Transactional
    public void deleteByMemberId(Long memberId) {

        postLikeRepository.deleteByMemberId(memberId);
        friendRepository.deleteByMemberId(memberId);
        likeCommentRepository.deleteByMemberId(memberId);
        commentRepository.deleteByMemberId(memberId);
        postRepository.deleteByMemberId(memberId);

        refreshTokenRepository.deleteByMemberId(memberId);
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new Exception404(ErrorCode.MEMBER_NOT_FOUND)
        );
        memberRepository.deleteById(memberId);
    }

}
