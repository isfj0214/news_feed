package com.example.news_feed.comment.service;

import com.example.news_feed.comment.dto.request.LikeCommentRequestDto;
import com.example.news_feed.comment.dto.response.LikeCommentResponseDto;
import com.example.news_feed.comment.entity.LikeComment;
import com.example.news_feed.comment.repository.LikeCommentRepository;
import com.example.news_feed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@RequestMapping("/likecomments")
public class LikeCommentService {

    private final LikeCommentRepository likeCommentRepository;
    private final MemberRepository memberRepository;

    //댓글 좋아요 생성
    public LikeCommentResponseDto addLike(LikeCommentRequestDto requestDto)
    {
        LikeComment likeComment = new LikeComment(requestDto.getMember(),requestDto.getComment());
        likeCommentRepository.save(likeComment);
        return new LikeCommentResponseDto(likeComment.getMember(),likeComment.getComment());
    }

    //(본인이)좋아요 누른 댓글만 조회
    public List<LikeCommentResponseDto> findByUser(Long id) {

    }


    //댓글 좋아요 취소(삭제)
}
