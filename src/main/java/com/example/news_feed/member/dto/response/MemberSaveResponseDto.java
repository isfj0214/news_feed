package com.example.news_feed.member.dto.response;

import com.example.news_feed.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberSaveResponseDto {
    private final Long id;
    private final String name;
    private final String email;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    @Builder
    public MemberSaveResponseDto(Long id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MemberSaveResponseDto buildDto (Member member) {
        return MemberSaveResponseDto.builder()
                .id(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getModifiedAt())
                .build();
    }

}
