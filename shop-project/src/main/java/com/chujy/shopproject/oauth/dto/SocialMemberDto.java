package com.chujy.shopproject.oauth.dto;

import com.chujy.shopproject.constant.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialMemberDto {

    private Role role;
    private String name;
    private String email;
    private String username;    // 소셜 로그인 사용자를 구분하는 특수한 값

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;

}
