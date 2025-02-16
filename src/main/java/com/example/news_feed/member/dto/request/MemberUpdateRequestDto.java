package com.example.news_feed.member.dto.request;

import lombok.Getter;

@Getter
public class MemberUpdateRequestDto {
    private String name;
    private String email;
    private String password;
}
