package com.example.news_feed.comment.dto.response;

import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private final Long postId;
    private final Long commentId;
    private final String userEmail;
    private final String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime modifiedAt;
    private final int likeCount;

    public CommentResponseDto(Long postId,Long commentId, String userEmail, String comment, LocalDateTime createdAt, LocalDateTime modifiedAt, int likeCount) {
        this.postId = postId;
        this.commentId = commentId;
        this.userEmail = userEmail;
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.likeCount = likeCount;
    }

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getPost().getId(),
                comment.getId(),
                comment.getMember().getEmail(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.getLikeCount()
        );
    }
}
