package com.example.news_feed.comment.controller;

import com.example.news_feed.comment.dto.request.LikeCommentRequestDto;
import com.example.news_feed.comment.dto.response.LikeCommentResponseDto;
import com.example.news_feed.comment.service.LikeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like_comments")
public class LikeCommentController {

    private final LikeCommentService likeCommentService;

    //댓글 좋아요 생성
    @PostMapping
    public ResponseEntity<LikeCommentResponseDto> addLike(@RequestBody LikeCommentRequestDto requestDto)
    {
       LikeCommentResponseDto likeCommentResponseDto =  likeCommentService.addLike(requestDto);
       return new ResponseEntity<>(likeCommentResponseDto, HttpStatus.CREATED);
    }

    //(본인이)좋아요 누른 댓글만 조회
    @GetMapping
    public ResponseEntity<List<LikeCommentResponseDto>> likeCommentFindByUser(@PathVariable Long id)
    {
        List<LikeCommentResponseDto> LikeCommentResponseDtoList = likeCommentService.findByUser(id);
        return new ResponseEntity<>(LikeCommentResponseDtoList,HttpStatus.OK);
    }

    //댓글 좋아요 취소(삭제)
}
