package com.example.news_feed.friend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FriendRequestResponseDto {
    private final Long fromId; // 친구 신청을 보내는 사람 id
    private final Long toId; // 친구 요청을 받는 사람 id
    private final LocalDateTime createAt;
    private final LocalDateTime updatedAt;

    @Builder
    public FriendRequestResponseDto(Long fromId, Long toId, LocalDateTime createAt, LocalDateTime updatedAt) {
        this.fromId = fromId;
        this.toId = toId;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
    }

}
