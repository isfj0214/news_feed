package com.example.news_feed.comment.dto.response;

import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.member.entity.Member;

public class LikeCommentResponseDto {
    private final Long memberId;
    private final String comment;

    public LikeCommentResponseDto(Long memberId, String comment) {
        this.memberId = memberId;
        this.comment = comment;
    }

}
