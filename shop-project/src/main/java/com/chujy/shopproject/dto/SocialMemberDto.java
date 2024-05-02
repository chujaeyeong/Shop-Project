package com.chujy.shopproject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialMemberDto {

    private String snsId;

    private String name;

    private String email;

    private String role;

    // 주소는 사용자가 추가로 입력
    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;
}
