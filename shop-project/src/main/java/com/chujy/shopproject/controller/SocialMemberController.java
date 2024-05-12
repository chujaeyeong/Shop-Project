package com.chujy.shopproject.controller;

import com.chujy.shopproject.domain.SocialMember;
import com.chujy.shopproject.dto.SocialMemberDto;
import com.chujy.shopproject.repository.SocialMemberRepository;
import com.chujy.shopproject.service.SocialMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class SocialMemberController {

    private final SocialMemberService socialMemberService;

    @GetMapping("/login/oauth2/code/{provider}")
    public String handleOAuth2Login(@AuthenticationPrincipal OAuth2User principal,
                                    @PathVariable String provider,
                                    RedirectAttributes redirectAttributes) {
        try {
            SocialMemberDto socialMemberDto = buildSocialMemberDto(principal, provider);
            SocialMember member = socialMemberService.registerOrAuthenticateSocialMember(socialMemberDto);

            return "redirect:/";  // 로그인 성공, 메인 페이지로 리다이렉션
        }

        catch (AlreadyRegisteredException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";  // 이미 일반 회원으로 가입된 경우 로그인 페이지로 리다이렉션
        }

        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "로그인 처리 중 문제가 발생했습니다.");
            return "redirect:/login";  // 기타 예외 처리
        }
    }

    @PostMapping("/social-save")
    public String saveSocialMember(@ModelAttribute("socialMemberDto") SocialMemberDto socialMemberDto,
                                   Model model) {
        try {
            SocialMember newMember = socialMemberService.saveOrUpdateSocialMember(socialMemberDto);
            return "redirect:/";  // 성공적으로 저장 후 메인 페이지로 리다이렉션
        }

        catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/socialMemberForm";  // 에러가 있을 경우 다시 폼 페이지로
        }
    }

    private SocialMemberDto buildSocialMemberDto(OAuth2User principal, String provider) {
        SocialMemberDto dto = new SocialMemberDto();
        dto.setEmail(principal.getAttribute("email"));
        dto.setName(principal.getAttribute("name"));
        String snsId = (provider.equals("google")) ? principal.getAttribute("sub") : principal.getAttribute("id");
        dto.setSnsId(provider + " " + snsId);  // SNS ID 구성
        dto.setRole("USER");
        return dto;
    }

}

