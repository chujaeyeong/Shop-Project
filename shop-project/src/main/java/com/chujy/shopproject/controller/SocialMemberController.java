package com.chujy.shopproject.controller;

import com.chujy.shopproject.domain.SocialMember;
import com.chujy.shopproject.dto.CustomOAuth2User;
import com.chujy.shopproject.dto.SocialMemberDto;
import com.chujy.shopproject.service.SocialMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class SocialMemberController {

    private final SocialMemberService socialMemberService;

    @GetMapping("/login/oauth2/code/{provider}")
    public String handleOAuth2Login(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                    @PathVariable String provider,
                                    RedirectAttributes redirectAttributes) {

        if (customOAuth2User != null) {
            return "redirect:/";  // 로그인 성공, 메인 페이지로 리다이렉션
        } else {
            redirectAttributes.addFlashAttribute("error", "로그인 처리 중 문제가 발생했습니다.");
            return "redirect:/login";  // 로그인 실패
        }
    }

    @PostMapping("/social-save")
    public String saveSocialMember(@ModelAttribute("socialMemberDto") SocialMemberDto socialMemberDto,
                                   RedirectAttributes redirectAttributes) {

        try {
            SocialMember newMember = socialMemberService.registerOrAuthenticateSocialMember(socialMemberDto);
            return "redirect:/";  // 성공적으로 저장 후 메인 페이지로 리다이렉션
        }

        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/socialMemberForm";  // 에러가 있을 경우 다시 폼 페이지로
        }
    }

    // 커스텀 예외 처리
    public static class AlreadyRegisteredException extends RuntimeException {
        public AlreadyRegisteredException(String message) {
            super(message);
        }
    }

}

