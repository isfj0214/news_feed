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
    public ResponseEntity<LikeCommentResponseDto> addLike(
            @PathVariable Long commentId,
            HttpServletRequest httpServletRequest //userId
    ) {
        Long memberId = Long.parseLong((String) httpServletRequest.getAttribute("memberId"));
        LikeCommentResponseDto likeCommentResponseDto = likeCommentService.addLike(commentId, memberId);
        return new ResponseEntity<>(likeCommentResponseDto, HttpStatus.CREATED);
    }

    //(본인이)좋아요 누른 댓글만 조회
    @GetMapping
    public ResponseEntity<List<LikeCommentResponseDto>> likeCommentFindByUser(
            @PathVariable Long commentId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = Long.parseLong((String) httpServletRequest.getAttribute("memberId"));
        List<LikeCommentResponseDto> LikeCommentResponseDtoList = likeCommentService.findByUser(memberId);
        return new ResponseEntity<>(LikeCommentResponseDtoList, HttpStatus.OK);
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
