package com.example.news_feed.comment.dto.response;

import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.member.entity.Member;

public class CommentResponseDto {
    private String contents;

    public CommentResponseDto(String contents) {
        this.contents = contents;
    }


    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment.getContents());
    }
}
