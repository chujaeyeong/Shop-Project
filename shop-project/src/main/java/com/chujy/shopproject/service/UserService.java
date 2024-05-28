package com.chujy.shopproject.service;

import com.chujy.shopproject.domain.AbstractUser;
import com.chujy.shopproject.domain.Member;
import com.chujy.shopproject.oauth.domain.SocialMember;
import com.chujy.shopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public AbstractUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다."));
    }

    public String getUserTypeByEmail(String email) {
        AbstractUser user = getUserByEmail(email);
        if (user instanceof Member) {
            return "Member";
        } else if (user instanceof SocialMember) {
            return "SocialMember";
        } else {
            throw new IllegalStateException("User type is unknown");
        }
    }

}
