package com.example.news_feed.member.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberPublicResponseDto extends MemberResponseDto{


    public MemberPublicResponseDto( String name, String email) {
        super(name, email);
    }
}
