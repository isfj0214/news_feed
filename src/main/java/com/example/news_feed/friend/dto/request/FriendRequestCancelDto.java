package com.example.news_feed.friend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FriendRequestCancelDto {
    private Long fromId; // 친구 신청을 보내는 사람 id
    @NotNull(message = "요청을 취소할 친구를 반드시 지정해 주세요.")
    private Long toId; // 친구 요청을 받는 사람 id
}
