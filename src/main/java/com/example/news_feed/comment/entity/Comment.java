package com.example.news_feed.comment.entity;

import com.example.news_feed.common.base.BaseEntity;
import com.example.news_feed.post.entity.Post;
import com.example.news_feed.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @Column(name = "like_count")
    private int likeCount = 0;

    public Comment(String comment, Member member, Post post) {
        this.comment = comment;
        this.member = member;
        this.post = post;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public Comment() {
    }

    public void setContents(String comment) {
        this.comment = comment;
        modifiedAt = LocalDateTime.now();
    }

    public void likeCountPlus() {
        this.likeCount++;
    }

    public void likeCountMinus() {
        this.likeCount--;
    }
}
