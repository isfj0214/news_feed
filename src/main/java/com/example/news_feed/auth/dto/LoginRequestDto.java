package com.example.news_feed.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @NotBlank(message = "email은 필수 입력 값입니다.")
    private String email;
    @NotBlank(message = "password는 필숫 입력 갑입니다.")
    private String password;
}
