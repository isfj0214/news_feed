package com.example.news_feed.comment.dto.response;

import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.member.entity.Member;

public class CommentResponseDto {
    private String contents;
    private Member member;
    public CommentResponseDto(String contents, Member member) {
        this.contents = contents;
        this.member = member;
    }


    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment.getContents(),comment.getMember());
    }
}
