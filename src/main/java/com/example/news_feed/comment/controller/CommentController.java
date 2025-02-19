package com.example.news_feed.comment.controller;

import com.example.news_feed.comment.dto.request.CommentRequestDto;
import com.example.news_feed.comment.dto.request.CommentUpdateRequestDto;
import com.example.news_feed.comment.dto.response.CommentResponseDto;
import com.example.news_feed.comment.service.CommentService;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception404;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping
    public ResponseEntity<CommentResponseDto> commentSave(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = Long.parseLong((String) httpServletRequest.getAttribute("memberId"));
        CommentResponseDto commentSaveResponseDto = commentService.creteComment(postId, memberId, requestDto);
        return new ResponseEntity<>(commentSaveResponseDto, HttpStatus.CREATED);
    }

    //댓글 단건 조회(By commentId)
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> commentFindById(@PathVariable Long commentId) {
        CommentResponseDto commentResponseDto = commentService.findById(commentId);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    //한 게시물에서 (본인이) 쓴 댓글만 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> commentFindByUser(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = Long.parseLong((String) httpServletRequest.getAttribute("memberId"));
        List<CommentResponseDto> CommentResponseDtoIdList = commentService.findAllById(postId, memberId, requestDto);
        if (CommentResponseDtoIdList == null) {
            throw new Exception404(ErrorCode.COMMENT_NOT_FOUND);
        }
        return new ResponseEntity<>(CommentResponseDtoIdList, HttpStatus.OK);
    }


    //댓글 전체 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> commentFindAll() {
        List<CommentResponseDto> CommentResponseDtoDtoList = commentService.findAll();

        return new ResponseEntity<>(CommentResponseDtoDtoList, HttpStatus.OK);
    }

    //댓글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTitle(
            @PathVariable Long postId,
            @PathVariable Long id,
            @RequestBody CommentUpdateRequestDto requestDto,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = Long.parseLong((String) httpServletRequest.getAttribute("memberId"));
        commentService.updateContents(postId, id, memberId, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long id,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = Long.parseLong((String) httpServletRequest.getAttribute("memberId"));
        commentService.deleteComment(id, postId, memberId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
