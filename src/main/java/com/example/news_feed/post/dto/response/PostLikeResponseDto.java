package com.example.news_feed.post.dto.response;

import lombok.Getter;

@Getter
public class PostLikeResponseDto {
    private final Long memberId;

    public PostLikeResponseDto(Long memberId){
        this.memberId = memberId;
    }
}
