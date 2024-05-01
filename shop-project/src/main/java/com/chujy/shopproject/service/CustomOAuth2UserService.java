package com.chujy.shopproject.service;

import com.chujy.shopproject.constant.Role;
import com.chujy.shopproject.domain.SocialMember;
import com.chujy.shopproject.dto.*;
import com.chujy.shopproject.repository.SocialMemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialMemberRepository socialMemberRepository;

    public CustomOAuth2UserService(SocialMemberRepository socialMemberRepository) {
        this.socialMemberRepository = socialMemberRepository;
    }

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
            throw new IllegalArgumentException("Unsupported registration ID");
        }

        // 리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String snsId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        // SNS 로그인을 했는지 확인
        SocialMember existData = socialMemberRepository.findBySnsId(snsId);

        if (existData == null) {

            SocialMember socialMember = new SocialMember();
            socialMember.setSnsId(snsId);
            socialMember.setEmail(oAuth2Response.getEmail());
            socialMember.setName(oAuth2Response.getName());
            socialMember.setRole(Role.USER);

            socialMemberRepository.save(socialMember);

            // DTO를 생성하고 사용자 정보 설정
            SocialMemberDto socialMemberDto = new SocialMemberDto();
            socialMemberDto.setSnsId(snsId);
            socialMemberDto.setName(oAuth2Response.getName());
            socialMemberDto.setEmail(oAuth2Response.getEmail());
            socialMemberDto.setRole(String.valueOf(Role.USER));

            return new CustomOAuth2User(socialMemberDto);

        }

        else {

            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            socialMemberRepository.save(existData);

            SocialMemberDto socialMemberDto = new SocialMemberDto();
            socialMemberDto.setSnsId(existData.getSnsId());
            socialMemberDto.setName(oAuth2Response.getName());
            socialMemberDto.setEmail(oAuth2Response.getEmail());
            socialMemberDto.setRole(String.valueOf(Role.USER));

            return new CustomOAuth2User(socialMemberDto);

        }


    }
}
