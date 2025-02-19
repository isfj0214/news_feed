package com.example.news_feed.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentUpdateRequestDto {
    private String contents;
    private String password;
}
