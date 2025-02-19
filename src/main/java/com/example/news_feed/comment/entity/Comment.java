package com.example.news_feed.comment.entity;

import com.example.news_feed.common.base.BaseEntity;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contents")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    //게시물 외래키 추가
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String contents, Member member, Post post) {

    }

    public Comment()
    {

    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

}
