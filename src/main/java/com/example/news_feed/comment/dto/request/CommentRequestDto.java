package com.example.news_feed.comment.dto.request;

import com.example.news_feed.post.entity.Post;
import lombok.Getter;
import com.example.news_feed.member.entity.Member;

@Getter
public class CommentRequestDto {
    private String comment;
}
