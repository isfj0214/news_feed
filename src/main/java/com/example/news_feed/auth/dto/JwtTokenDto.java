package com.example.news_feed.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class JwtTokenDto {

    private String accessToken;
    private String refreshToken;

    @Builder
    public JwtTokenDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken =refreshToken;
    }

}
