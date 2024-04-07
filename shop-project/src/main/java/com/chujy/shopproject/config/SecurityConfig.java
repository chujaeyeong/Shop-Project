package com.chujy.shopproject.config;

import com.chujy.shopproject.service.CustomOAuth2UserService;
import com.chujy.shopproject.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MemberService memberService;

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin((formLogin) -> formLogin
                        .usernameParameter("email")    // 로그인 시 사용할 파라미터로 email 을 사용
                        .failureUrl("/members/login/error")     // 로그인 실패 시 이동할 페이지
                        .loginPage("/members/login")    // 로그인 페이지 설정
                        .failureHandler(customAuthenticationFailureHandler())       // 로그인 실패 시 로그 출력을 위한 설정
                        .defaultSuccessUrl("/"))        // 로그인 성공시 이동할 페이지

                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))     // 로그아웃 url 설정
                        .logoutSuccessUrl("/")          // 로그아웃 성공 시 이동할 url
                        .invalidateHttpSession(true))   // 기존에 생성된 사용자 세션도 invalidateHttpSession 을 통해 삭제하도록 처리
        ;

        // 특정 URL에 대한 권한 설정
        // permitAll() : 모든 사용자가 인증(로그인) 없이 해당 경로에 접근
        // hasRole("ADMIN") : /admin 으로 시작하는 경로는 ADMIN Role 일 경우에만 접근 가능
        // 명시한 나머지 경로는 모두 인증을 요구하도록 설정
        http.authorizeHttpRequests((authorizeRequests) -> {
            authorizeRequests
                    .requestMatchers("/favicon.ico", "/error").permitAll()
                    .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                    .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated();
        });

        // 인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러 등록
        http.exceptionHandling(authenticationManager -> authenticationManager
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        // OAuth2 설정
        http.oauth2Login(oauth2Login -> oauth2Login
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService) // OAuth2 사용자 서비스 설정
                )
                .loginPage("/members/login")
                .successHandler((request, response, authentication) -> {
                    // 로그인 성공 후의 추가 작업을 여기에 구현
                    // 예: 사용자 리디렉션, 세션 설정 등
                    response.sendRedirect("/"); // 로그인 성공 시 리디렉션 할 주소
                })
        );

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(memberService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
