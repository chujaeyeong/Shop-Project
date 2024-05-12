package com.chujy.shopproject.oauth.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class OAuth2Controller {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);

    @GetMapping("/login/oauth2/code/google")
    public ModelAndView googleLoginSuccess(OAuth2AuthenticationToken authentication) {
        OidcUser user = (OidcUser) authentication.getPrincipal();
        logger.info("Google login successful for user: {}, email: {}", user.getName(), user.getEmail());

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/login/oauth2/code/naver")
    public ModelAndView naverLoginSuccess(OAuth2AuthenticationToken authentication) {
        OidcUser user = (OidcUser) authentication.getPrincipal();
        logger.info("Naver login successful for user: {}, email: {}", user.getName(), user.getEmail());

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/loginFailure")
    public ModelAndView loginFailure() {
        logger.error("Login failed - unable to authenticate user.");

        return new ModelAndView("redirect:/member/memberLoginForm");  // 실패 시 현재 로그인 페이지로 리다이렉션
    }

}
