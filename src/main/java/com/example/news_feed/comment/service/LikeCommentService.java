package com.example.news_feed.comment.service;

import com.example.news_feed.comment.dto.response.LikeCommentResponseDto;
import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.comment.entity.LikeComment;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.comment.repository.LikeCommentRepository;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception400;
import com.example.news_feed.common.error.exception.Exception403;
import com.example.news_feed.common.error.exception.Exception404;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import com.example.news_feed.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeCommentService {

    private final LikeCommentRepository likeCommentRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    //댓글 좋아요 생성
    @Transactional
    public void addLike(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new Exception404(ErrorCode.COMMENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        LikeComment likeComment = new LikeComment(member, comment);

        if(member.getId().equals(comment.getMember().getId())){
            throw new Exception400(ErrorCode.SELF_LIKE_REQUEST);
        }

        if (likeCommentRepository.existsByMemberAndComment(member, comment)) {
            throw new Exception400(ErrorCode.ALREADY_EXIST_LIKE_REQUEST);
        }

        likeCommentRepository.save(likeComment);
        comment.likeCountPlus();
        commentRepository.save(comment);
        log.info("{} {}", likeComment.getId().toString(), String.valueOf(comment.getLikeCount()));

    }

    //댓글 좋아요 취소(삭제)
    @Transactional
    public void removeLike(Long likeCommentId, Long commentId, Long memberId) {
        LikeComment likeComment = likeCommentRepository.findById(likeCommentId).orElseThrow(()->new Exception404(ErrorCode.LIKECOMMENT_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new Exception404(ErrorCode.COMMENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));

        likeCommentRepository.delete(likeComment);
        comment.likeCountMinus();
        commentRepository.save(comment);
    }
}
