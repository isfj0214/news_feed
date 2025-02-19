package com.example.news_feed.comment.dto.request;

import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.member.entity.Member;
import lombok.Getter;

@Getter
public class LikeCommentRequestDto {
    private Comment comment;
}
