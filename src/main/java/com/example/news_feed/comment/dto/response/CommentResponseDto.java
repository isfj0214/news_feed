package com.example.news_feed.comment.dto.response;

import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.member.entity.Member;

import java.time.LocalDateTime;

public class CommentResponseDto {

    private String postTitle;
    private String writerEmail;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCount;

    public CommentResponseDto(String postTitle, String writerEmail, String comment, LocalDateTime createdAt, LocalDateTime modifiedAt, int likeCount) {
        this.postTitle = postTitle;
        this.writerEmail = writerEmail;
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }


    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment.getPost().getTitle(),comment.getMember().getEmail(),comment.getComment(),comment.getCreatedAt(),comment.getModifiedAt(), comment.getLikeCount());
    }
}
