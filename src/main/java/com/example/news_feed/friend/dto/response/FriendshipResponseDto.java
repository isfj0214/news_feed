package com.example.news_feed.friend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FriendshipResponseDto {
    private final Long fromId; // 친구 신청을 보내는 사람 id
    private final Long toId; // 친구 요청을 받는 사람 id

    @Builder
    public FriendshipResponseDto(Long fromId, Long toId) {
        this.fromId = fromId;
        this.toId = toId;
    }

}
