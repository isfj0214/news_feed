package com.example.news_feed.comment.entity;

import com.example.news_feed.comment.dto.request.LikeCommentRequestDto;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="comment_id")
    private Comment comment;

    public LikeComment(Member member, Comment comment) {
        this.member=member;
        this.comment = comment;
    }


    //게시물 추가


}
