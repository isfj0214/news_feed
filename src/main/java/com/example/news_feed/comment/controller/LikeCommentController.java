package com.example.news_feed.comment.controller;

import com.example.news_feed.comment.dto.request.LikeCommentRequestDto;
import com.example.news_feed.comment.dto.response.LikeCommentResponseDto;
import com.example.news_feed.comment.service.LikeCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments/{commentId}/likes")
public class LikeCommentController {

    private final LikeCommentService likeCommentService;

    //댓글 좋아요 생성
    @PostMapping
    public void addLike(
            @PathVariable Long commentId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = Long.parseLong((String) httpServletRequest.getAttribute("memberId"));
        likeCommentService.addLike(commentId,memberId);
    }

    //댓글 좋아요 취소(삭제)
    @DeleteMapping("/{id}")
    public void removeLike(
            @PathVariable Long id,
            @PathVariable Long commentId,
            HttpServletRequest httpServletRequest
    ){
        Long memberId = Long.parseLong((String)httpServletRequest.getAttribute("memberId"));
        likeCommentService.removeLike(id, commentId,memberId);
    }
}
