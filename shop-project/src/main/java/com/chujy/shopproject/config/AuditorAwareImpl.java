package com.chujy.shopproject.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        // 현재 로그인 한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 지정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("anonymousUser");
        }
        return Optional.ofNullable(authentication.getName());
    }

}
