package com.example.news_feed.comment.dto.response;

import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class CommentResponseDto {

    private String comment;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    private int likeCount;

    public CommentResponseDto(String comment, LocalDateTime createdAt, LocalDateTime modifiedAt, int likeCount) {
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.likeCount = likeCount;
    }


    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment.getComment(),comment.getCreatedAt(),comment.getModifiedAt(), comment.getLikeCount());
    }
}
