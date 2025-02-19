package com.example.news_feed.friend.dto.response;

import com.example.news_feed.member.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class FriendResponseDto {
    private final Long id;
    private final String name;

    public FriendResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<FriendResponseDto> membersBuildToDto(List<Member> members){
        List<FriendResponseDto> responseDtos = new ArrayList<>();
        for (Member member : members){
            responseDtos.add(new FriendResponseDto(member.getId(), member.getName()));
        }
        return responseDtos;
    }
}
