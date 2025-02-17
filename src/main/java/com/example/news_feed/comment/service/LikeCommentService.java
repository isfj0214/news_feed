package com.example.news_feed.comment.service;

import com.example.news_feed.comment.repository.LikeCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeCommentService {

    private final LikeCommentRepository likeCommentRepository;

    //댓글 좋아요 생성

    //(본인이)좋아요 누른 댓글만 조회

    //댓글 좋아요 취소(삭제)
}
