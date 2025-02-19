package com.example.news_feed.friend.dto.request;

import lombok.Getter;

@Getter
public class FriendRequestCancelDto {
    private Long fromId; // 친구 신청을 보내는 사람 id
    private Long toId; // 친구 요청을 받는 사람 id
}
