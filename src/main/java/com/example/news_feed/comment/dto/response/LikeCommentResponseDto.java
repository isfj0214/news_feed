package com.example.news_feed.comment.dto.response;

import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.member.entity.Member;

public class LikeCommentResponseDto {
    private Member member;
    private Comment comment;

    public LikeCommentResponseDto(Member member, Comment comment) {
        this.member = member;
        this.comment = comment;
    }

}
