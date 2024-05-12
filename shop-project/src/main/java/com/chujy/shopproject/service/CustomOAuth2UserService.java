package com.chujy.shopproject.service;

import com.chujy.shopproject.domain.SocialMember;
import com.chujy.shopproject.dto.*;
import com.chujy.shopproject.repository.SocialMemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialMemberRepository socialMemberRepository;

    public CustomOAuth2UserService(SocialMemberRepository socialMemberRepository) {
        this.socialMemberRepository = socialMemberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = parseOAuth2Response(registrationId, oAuth2User.getAttributes());
        String snsId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        // 사용자 조회 후 존재하지 않으면 DTO 생성하여 반환
        SocialMember existData = socialMemberRepository.findBySnsId(snsId);
        if (existData == null) {
            SocialMemberDto socialMemberDto = new SocialMemberDto();
            socialMemberDto.setSnsId(snsId);
            socialMemberDto.setName(oAuth2Response.getName());
            socialMemberDto.setEmail(oAuth2Response.getEmail());
            socialMemberDto.setRole("USER");
            return new CustomOAuth2User(socialMemberDto);
        }

        return new CustomOAuth2User(mapToSocialMemberDto(existData));
    }

    private OAuth2Response parseOAuth2Response(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return new GoogleResponse(attributes);
        } else if (registrationId.equals("naver")) {
            return new NaverResponse(attributes);
        } else {
            throw new IllegalArgumentException("Unsupported registration ID");
        }
    }

    private SocialMemberDto mapToSocialMemberDto(SocialMember member) {
        SocialMemberDto dto = new SocialMemberDto();
        dto.setSnsId(member.getSnsId());
        dto.setName(member.getName());
        dto.setEmail(member.getEmail());
        dto.setRole(member.getRole().name());
        return dto;
    }

}
