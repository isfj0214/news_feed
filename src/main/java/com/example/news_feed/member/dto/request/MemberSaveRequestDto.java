package com.example.news_feed.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberSaveRequestDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(min = 2, max =  18, message = "이름을 2~18자 이내로 작성해주세요.")
    private String name;

    @NotBlank(message = "email은 필수 입력 값입니다.")
    @Email(message = "유효한 email 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상 입력해주세요.")
    private String password;
}
