package com.chujy.shopproject.service;

import com.chujy.shopproject.domain.AbstractUser;
import com.chujy.shopproject.oauth.dto.SocialMemberDto;
import com.chujy.shopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SocialMemberService {

    private final UserRepository userRepository;

    // ID로 회원 조회
    public AbstractUser findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

    // 소셜 회원 정보 업데이트
    public AbstractUser updateSocialMember(Long id, SocialMemberDto socialMemberDto) {
        AbstractUser user = findById(id);

        // 주소 업데이트
        user.setAddress(socialMemberDto.getAddress());

        // 변경된 정보 저장
        return userRepository.save(user);
    }

}
