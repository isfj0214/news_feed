package com.example.news_feed.comment.controller;

import com.example.news_feed.comment.service.LikeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class LikeCommentController {

    private final LikeCommentService likeCommentService;

    //댓글 좋아요 생성

    //(본인이)좋아요 누른 댓글만 조회

    //댓글 좋아요 취소(삭제)
}
