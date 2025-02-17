package com.example.news_feed.friend.entity;

import com.example.news_feed.common.base.BaseEntity;
import com.example.news_feed.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "friend")
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id")
    private Member member;

    private Boolean isFriend;

    public Friend(Long fromId, Member member, Boolean isFriend) {
        this.fromId = fromId;
        this.member = member;
        this.isFriend = isFriend;
    }

    public void update(Boolean isFriend) {
        this.isFriend = isFriend;
    }

}
