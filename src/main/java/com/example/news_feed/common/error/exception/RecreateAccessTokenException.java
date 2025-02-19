package com.example.news_feed.common.error.exception;

import lombok.Getter;

@Getter
public class RecreateAccessTokenException extends RuntimeException{
    private String accessToken;
    public RecreateAccessTokenException(String accessToken){
        this.accessToken = accessToken;
    }
}
