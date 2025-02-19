package com.example.news_feed.friend.dto.response;

import com.example.news_feed.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FriendRequestResponseDto {
    private final Long fromId; // 친구 신청을 보내는 사람 id
    private final Long toId; // 친구 요청을 받는 사람 id

    @Builder
    public FriendRequestResponseDto(Long fromId, Long toId) {
        this.fromId = fromId;
        this.toId = toId;
    }

}
