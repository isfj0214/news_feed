package com.example.news_feed.auth.token;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class JwtToken {

    private String accessToken;
    private String refreshToken;

    public JwtToken(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken =refreshToken;
    }

}
