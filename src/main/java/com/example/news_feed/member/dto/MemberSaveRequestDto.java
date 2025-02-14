package com.example.news_feed.member.dto;

import lombok.Getter;

@Getter
public class MemberSaveRequestDto {
    private String name;
    private String email;
    private String password;
}
