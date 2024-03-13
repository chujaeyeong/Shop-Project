package com.chujy.shopproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {     // 등록자와 수정자를 처리해주는 AuditorAware을 빈으로 등록
        return new AuditorAwareImpl();
    }
}
