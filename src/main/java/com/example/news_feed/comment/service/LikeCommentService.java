package com.example.news_feed.comment.service;

import com.example.news_feed.comment.dto.request.LikeCommentRequestDto;
import com.example.news_feed.comment.dto.response.LikeCommentResponseDto;
import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.comment.entity.LikeComment;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.comment.repository.LikeCommentRepository;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception404;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeCommentService {

    private final LikeCommentRepository likeCommentRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    //댓글 좋아요 생성
    public LikeCommentResponseDto addLike(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new Exception404(ErrorCode.COMMENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        LikeComment likeComment = new LikeComment(member, comment);
        likeCommentRepository.save(likeComment);
        comment.likeCountPlus();
        return new LikeCommentResponseDto(likeComment.getMember(), likeComment.getComment());
    }

    //(본인이)좋아요 누른 댓글만 조회
    public List<LikeCommentResponseDto> findByUser(Long userid) {
        return null;
    }

    //댓글 좋아요 취소(삭제)
    public void removeLike(Long likeCommentId, Long commentId, Long memberId) {
        LikeComment likeComment = likeCommentRepository.findById(likeCommentId).orElseThrow(()->new Exception404(ErrorCode.LIKECOMMENT_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new Exception404(ErrorCode.COMMENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));

        likeCommentRepository.delete(likeComment);
        comment.likeContMinus();
    }
}
