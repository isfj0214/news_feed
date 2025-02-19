package com.example.news_feed.comment.entity;

import com.example.news_feed.common.base.BaseEntity;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.post.entity.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",referencedColumnName = "id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",referencedColumnName = "id", nullable = false)
    private Post post;

    @Column(name="like_count")
    private int likeCount;

    public Comment(String comment, Member member, Post post) {
        this.comment = comment;
        this.member = member;
        this.post = post;
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

    public void likeCountMinus()
    {
        this.likeCount--;
    }

}
