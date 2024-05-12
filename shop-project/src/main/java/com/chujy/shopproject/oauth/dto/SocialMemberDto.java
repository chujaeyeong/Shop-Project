package com.chujy.shopproject.oauth.dto;

import com.chujy.shopproject.constant.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialMemberDto {

    private Role role;
    private String name;
    private String username;

}
