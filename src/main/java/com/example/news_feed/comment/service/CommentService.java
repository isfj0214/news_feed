package com.example.news_feed.comment.service;


import com.example.news_feed.comment.dto.request.CommentRequestDto;
import com.example.news_feed.comment.dto.request.CommentUpdateRequestDto;
import com.example.news_feed.comment.dto.response.CommentResponseDto;
import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.comment.repository.LikeCommentRepository;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception403;
import com.example.news_feed.common.error.exception.Exception404;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import com.example.news_feed.post.entity.Post;
import com.example.news_feed.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final LikeCommentRepository likeCommentRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDto creteComment(Long postId, Long memberId, CommentRequestDto requestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception404(ErrorCode.POST_NOT_FOUND));
        Comment comment = new Comment(requestDto.getComment(), member, post);


        commentRepository.save(comment);

        return new CommentResponseDto(
                comment.getPost().getId(),
                comment.getId(),
                comment.getMember().getEmail(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.getLikeCount());
    }

    //댓글 단건 조회(BycommentId)
    @Transactional
    public CommentResponseDto findById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new Exception404(ErrorCode.COMMENT_NOT_FOUND));


        return new CommentResponseDto(
                comment.getPost().getId(),
                comment.getId(),
                comment.getMember().getEmail(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.getLikeCount()
                );
    }

    //모든 게시물의 댓글 전체 조회
    @Transactional
    public List<CommentResponseDto> findAll() {
        return commentRepository.findAll().stream().map(CommentResponseDto::toDto).toList();
    }

    //댓글 수정
    @Transactional
    public void updateContents(Long postId, Long commentId, Long memberId, CommentUpdateRequestDto requestDto) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new Exception404(ErrorCode.COMMENT_NOT_FOUND));
        Member user = memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        Member writer = findComment.getMember();
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception404(ErrorCode.POST_NOT_FOUND));

        if(!postId.equals(post.getId()))
        {
            throw new Exception404(ErrorCode.COMMENT_NOT_FOUND);
        }
        if (!post.getMember().getEmail().equals(user.getEmail())) {
            throw new Exception403(ErrorCode.COMMENT_ACCESS_DENIED);
        } else if (!writer.getEmail().equals(user.getEmail())) {
            throw new Exception403(ErrorCode.COMMENT_ACCESS_DENIED);
        }
        findComment.setContents(requestDto.getContents());
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long postId, Long memberId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new Exception404(ErrorCode.COMMENT_NOT_FOUND));
        Member user = memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        Member writer = memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception404(ErrorCode.POST_NOT_FOUND));

        if(!postId.equals(post.getId()))
        {
            throw new Exception404(ErrorCode.COMMENT_NOT_FOUND);
        }
        if (!post.getMember().getEmail().equals(user.getEmail())) {
            throw new Exception403(ErrorCode.COMMENT_ACCESS_DENIED);
        } else if (!writer.getEmail().equals(user.getEmail())) {
            throw new Exception403(ErrorCode.COMMENT_ACCESS_DENIED);
        }
        likeCommentRepository.deleteByCommentId(commentId);
        commentRepository.delete(findComment);
    }

}
