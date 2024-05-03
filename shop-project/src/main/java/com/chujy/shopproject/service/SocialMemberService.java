package com.chujy.shopproject.service;

import com.chujy.shopproject.constant.Role;
import com.chujy.shopproject.domain.Member;
import com.chujy.shopproject.domain.SocialMember;
import com.chujy.shopproject.dto.SocialMemberDto;
import com.chujy.shopproject.repository.MemberRepository;
import com.chujy.shopproject.repository.SocialMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SocialMemberService {

    private final SocialMemberRepository socialMemberRepository;
    private final MemberRepository memberRepository;

    public SocialMember registerOrAuthenticateSocialMember(SocialMemberDto socialMemberDto) throws AlreadyRegisteredException {

        SocialMember socialMember = socialMemberRepository.findBySnsId(socialMemberDto.getSnsId())
                .orElse(null);

        if (socialMember != null) {
            // 이미 소셜 로그인을 한 적이 있는 회원이므로 로그인 처리
            return socialMember;
        } else {
            Member existingMember = memberRepository.findByEmail(socialMemberDto.getEmail());
            if (existingMember != null) {
                // 이미 일반 회원으로 가입된 경우
                throw new AlreadyRegisteredException("이미 일반 회원으로 가입되어 있습니다.");
            } else {
                // 소셜 로그인이 처음이므로 신규 등록
                return registerNewSocialMember(socialMemberDto);
            }
        }

    }

    private SocialMember registerNewSocialMember(SocialMemberDto socialMemberDto) {
        SocialMember newMember = SocialMember.createSocialMember(socialMemberDto);
        return socialMemberRepository.save(newMember);
    }

    // 커스텀 예외 처리
    public static class AlreadyRegisteredException extends RuntimeException {
        public AlreadyRegisteredException(String message) {
            super(message);
        }
    }

}
