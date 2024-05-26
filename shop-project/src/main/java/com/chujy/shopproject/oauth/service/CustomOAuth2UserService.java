package com.chujy.shopproject.oauth.service;

import com.chujy.shopproject.constant.Role;
import com.chujy.shopproject.oauth.domain.SocialMember;
import com.chujy.shopproject.oauth.dto.*;
import com.chujy.shopproject.oauth.repository.SocialMemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final SocialMemberRepository socialMemberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2User loaded: {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        // 리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        // 유저 정보를 DB에 저장하는 로직
        SocialMember existData = socialMemberRepository.findByUsername(username);
        if (existData == null) {
            SocialMember socialMember = new SocialMember();
            socialMember.setUsername(username);
            if (oAuth2Response.getEmail() != null && !oAuth2Response.getEmail().isEmpty()) {
                socialMember.setEmail(oAuth2Response.getEmail());
            } else {
                socialMember.setEmail(username);  // 이메일이 없는 경우 username을 이메일로 사용
            }
            socialMember.setName(oAuth2Response.getName());
            socialMember.setRole(Role.ROLE_USER);

            socialMemberRepository.save(socialMember);

            SocialMemberDto socialMemberDto = new SocialMemberDto();
            socialMemberDto.setUsername(username);
            socialMemberDto.setName(oAuth2Response.getName());
            socialMemberDto.setEmail(socialMember.getEmail());
            socialMemberDto.setRole(Role.ROLE_USER);

            log.info("Email from OAuth2Response: {}", oAuth2Response.getEmail());
            log.info("Name from OAuth2Response: {}", oAuth2Response.getName());

            return new CustomOAuth2User(socialMemberDto);

        } else {
            existData.setEmail(oAuth2Response.getEmail() != null && !oAuth2Response.getEmail().isEmpty() ? oAuth2Response.getEmail() : username);
            existData.setName(oAuth2Response.getName());

            socialMemberRepository.save(existData);

            SocialMemberDto socialMemberDto = new SocialMemberDto();
            socialMemberDto.setUsername(existData.getUsername());
            socialMemberDto.setName(oAuth2Response.getName());
            socialMemberDto.setEmail(existData.getEmail());
            socialMemberDto.setRole(existData.getRole());

            log.info("Email from OAuth2Response: {}", oAuth2Response.getEmail());
            log.info("Name from OAuth2Response: {}", oAuth2Response.getName());

            return new CustomOAuth2User(socialMemberDto);
        }

    }
}
