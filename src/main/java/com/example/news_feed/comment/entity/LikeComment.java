package com.example.news_feed.comment.entity;

import com.example.news_feed.comment.dto.request.LikeCommentRequestDto;
import com.example.news_feed.common.base.BaseEntity;
import com.example.news_feed.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "likecomments")
public class LikeComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",referencedColumnName = "id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id",referencedColumnName = "id", nullable = false)
    private Comment comment;


    public LikeComment(Member member, Comment comment) {
        this.member = member;
        this.comment = comment;
    }

    public LikeComment() {

    }
}
