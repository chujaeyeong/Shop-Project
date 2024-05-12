package com.chujy.shopproject.dto;

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
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return socialMemberDto.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return socialMemberDto.getName();
    }

    public String getEmail() {
        return socialMemberDto.getEmail();
    }
}
