package com.example.news_feed.comment.entity;

import com.example.news_feed.common.base.BaseEntity;
import com.example.news_feed.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "likecomments")
public class LikeComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contents")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="comment_id")
    private Comment comment;


    //게시물 추가


}
