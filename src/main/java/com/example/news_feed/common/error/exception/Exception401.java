package com.example.news_feed.common.error.exception;


import com.example.news_feed.common.error.ErrorCode;

public class Exception401 extends RuntimeException{
    private ErrorCode errorCode;

    public Exception401(ErrorCode errorCode){
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return this.errorCode;
    }
}
