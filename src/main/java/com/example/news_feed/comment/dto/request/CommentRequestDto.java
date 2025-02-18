package com.example.news_feed.comment.dto.request;

import lombok.Getter;
import com.example.news_feed.member.entity.Member;

@Getter
public class CommentRequestDto {
    private String contents;
    private Member member;

}
