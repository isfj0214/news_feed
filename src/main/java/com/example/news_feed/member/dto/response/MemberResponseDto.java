package com.example.news_feed.member.dto.response;

import lombok.Getter;

@Getter
public class MemberResponseDto {
    private final String name;
    private final String email;

    public MemberResponseDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
