package com.chujy.shopproject.service;

import com.chujy.shopproject.domain.Member;
import com.chujy.shopproject.domain.SocialMember;
import com.chujy.shopproject.repository.MemberRepository;
import com.chujy.shopproject.repository.SocialMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SocialMemberService implements UserDetailsService {

    private final SocialMemberRepository socialMemberRepository;
    private final MemberRepository memberRepository;

    public SocialMember saveSocialMember(SocialMember socialMember) {
        validateDuplicateSocialMember(socialMember);
        return socialMemberRepository.save(socialMember);
    }

    // 이미 가입한 회원일 경우 예외 발생
    private void validateDuplicateSocialMember(SocialMember socialMember) {
        SocialMember findSocialMember = socialMemberRepository.findBySnsId(socialMember.getSnsId());
        if (findSocialMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String snsId) throws UsernameNotFoundException {
        SocialMember socialMember = socialMemberRepository.findBySnsId(snsId);

        if (socialMember == null) {
            throw new UsernameNotFoundException(snsId);
        }

        // SNS 플랫폼에서 받는 email 조회
        String email = socialMember.getEmail();

        // email을 통해 일반 사용자인지 확인
        if (email != null) {
            Member existingMember = memberRepository.findByEmail(email);
            if (existingMember != null) {
                throw new IllegalStateException("이미 가입된 회원입니다.");
            }
        }

        return User.builder()
                .username(email)
                .roles(socialMember.getRole().toString())
                .build();
    }

}
