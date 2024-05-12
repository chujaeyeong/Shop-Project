package com.chujy.shopproject.dto;

import com.chujy.shopproject.constant.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final SocialMemberDto socialMemberDto;

    public CustomOAuth2User(SocialMemberDto socialMemberDto) {
        this.socialMemberDto = socialMemberDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", socialMemberDto.getEmail());
        attributes.put("name", socialMemberDto.getName());
        attributes.put("snsId", socialMemberDto.getSnsId());
        attributes.put("role", Role.USER);

        return attributes;
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
