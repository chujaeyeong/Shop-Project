package com.chujy.shopproject.oauth.dto;

import com.chujy.shopproject.constant.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final SocialMemberDto socialMemberDto;
    public CustomOAuth2User(SocialMemberDto socialMemberDto) {
        this.socialMemberDto = socialMemberDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        // google 이 가지는 Attribute랑 naver 가 가지는 Attribute 가 달라서 하단에 따로 구현하고, 이 부분은 null 로 유지
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Role role = socialMemberDto.getRole();
        String authority = role.name().startsWith("ROLE_") ? role.name() : "ROLE_" + role.name();
        authorities.add(() -> authority);
        return authorities;
    }

    @Override
    public String getName() {
        if (socialMemberDto.getEmail() != null && !socialMemberDto.getEmail().isEmpty()) {
            return socialMemberDto.getEmail();
        } else {
            return socialMemberDto.getUsername(); // 이메일이 없는 경우 username 반환
        }
    }

    public String getUsername() {
        return socialMemberDto.getUsername();
    }

    public String getEmail() {
        return socialMemberDto.getEmail();
    }

}
