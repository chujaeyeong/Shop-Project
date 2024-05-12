package com.chujy.shopproject.service;

import com.chujy.shopproject.domain.SocialMember;
import com.chujy.shopproject.dto.*;
import com.chujy.shopproject.repository.SocialMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final SocialMemberService socialMemberService;

    public CustomOAuth2UserService(SocialMemberService socialMemberService) {
        this.socialMemberService = socialMemberService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        logger.debug("Loading user from OAuth2UserRequest: {}", userRequest);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        logger.debug("Loaded OAuth2User: {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        logger.debug("Registration ID: {}", registrationId);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        logger.debug("OAuth2User attributes: {}", attributes);

        OAuth2Response oAuth2Response = parseOAuth2Response(registrationId, attributes);
        String snsId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        SocialMemberDto socialMemberDto = new SocialMemberDto();
        socialMemberDto.setEmail(oAuth2Response.getEmail());
        socialMemberDto.setName(oAuth2Response.getName());
        socialMemberDto.setSnsId(snsId);
        socialMemberDto.setRole("USER");  // Default role 설정

        SocialMember socialMember = socialMemberService.registerOrAuthenticateSocialMember(socialMemberDto);
        return new CustomOAuth2User(socialMemberDto);
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

}
