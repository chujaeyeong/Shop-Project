package com.chujy.shopproject.service;

import com.chujy.shopproject.domain.AbstractUser;
import com.chujy.shopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    // ID로 회원 조회
    public AbstractUser findByUserId(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다."));
    }

    // email 로 회원 조회
    public AbstractUser findByEmail(String email) {
        AbstractUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다."));
        return user;
    }

}
