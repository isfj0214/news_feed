package com.example.news_feed.comment.entity;

import com.example.news_feed.common.base.BaseEntity;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    private int likeCount;

    public Comment(String comment, Member member, Post post) {

    }

    public Comment()
    {

    }

    public void setContents(String comment)
    {
        this.comment = comment;
    }

    public void likeCountPlus()
    {
        this.likeCount++;
    }

    public void likeContMinus()
    {
        this.likeCount--;
    }

}
