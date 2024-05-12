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

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> collection = new ArrayList<>();
//
//        collection.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return socialMemberDto.getRole();
//            }
//        });
//
//        return collection;
//    }

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
        return socialMemberDto.getName();
    }

    public String getUsername() {
        return socialMemberDto.getUsername();
    }
}
