package com.chujy.shopproject.oauth.service;

import com.chujy.shopproject.constant.Role;
import com.chujy.shopproject.oauth.domain.SocialMember;
import com.chujy.shopproject.oauth.dto.*;
import com.chujy.shopproject.oauth.repository.SocialMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialMemberRepository socialMemberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        // 리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        // 유저 정보를 DB에 저장하는 로직
        // 1. 이미 로그인해서 유저가 DB에 존재 하는지 확인
        SocialMember existData = socialMemberRepository.findByUsername(username);
        if (existData == null) { // 2. 없으면 DB에 유저 정보 저장

            SocialMember socialMember = new SocialMember();
            socialMember.setUsername(username);
            socialMember.setEmail(oAuth2Response.getEmail());
            socialMember.setName(oAuth2Response.getName());
            socialMember.setRole(Role.ROLE_USER);
//            socialMember.setRole("ROLE_USER");

            socialMemberRepository.save(socialMember);

            SocialMemberDto socialMemberDto = new SocialMemberDto();
            socialMemberDto.setUsername(username);
            socialMemberDto.setName(oAuth2Response.getName());
            socialMemberDto.setRole(Role.ROLE_USER);
//            socialMemberDto.setRole("ROLE_USER");

            return new CustomOAuth2User(socialMemberDto);

        }

        else { // 3. 이미 있으면 이름, 이메일값만 update (username는 유지)

            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            socialMemberRepository.save(existData);

            SocialMemberDto socialMemberDto = new SocialMemberDto();
            socialMemberDto.setUsername(existData.getUsername());
            socialMemberDto.setName(oAuth2Response.getName());
            socialMemberDto.setRole(existData.getRole());

            return new CustomOAuth2User(socialMemberDto);
        }

    }

}
