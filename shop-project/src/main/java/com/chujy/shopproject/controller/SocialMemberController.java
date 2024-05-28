package com.chujy.shopproject.controller;

import com.chujy.shopproject.domain.AbstractUser;
import com.chujy.shopproject.oauth.dto.CustomOAuth2User;
import com.chujy.shopproject.oauth.dto.SocialMemberDto;
import com.chujy.shopproject.service.ProfileService;
import com.chujy.shopproject.service.SocialMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/social")
public class SocialMemberController {

    private final SocialMemberService socialMemberService;
    private final ProfileService profileService;

    // 소셜 회원 정보 수정 폼
    @GetMapping("/member/updateForm")
    public String updateSocialMemberForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AbstractUser user = null;

        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            // Authentication 객체에서 이메일을 가져오기
            String email = ((CustomOAuth2User) authentication.getPrincipal()).getEmail();
            // 이메일을 사용하여 AbstractUser 객체 가져오기
            user = profileService.findByEmail(email);
        } else {
            throw new IllegalStateException("Authentication principal is not of expected type.");
        }

        if (user == null) {
            throw new IllegalStateException("Authenticated user not found");
        }

        SocialMemberDto socialMemberDto = new SocialMemberDto();
        socialMemberDto.setEmail(user.getEmail());
        socialMemberDto.setName(user.getName());
        socialMemberDto.setAddress(user.getAddress());
        model.addAttribute("socialMemberDto", socialMemberDto);

        return "/social/memberUpdateForm";
    }

    // 소셜 회원 정보 수정 처리
    @PostMapping("/member/update")
    public String updateSocialMember(@Valid SocialMemberDto socialMemberDto,
                                     BindingResult result,
                                     Authentication authentication,
                                     Model model) {
        if (result.hasErrors()) {
            model.addAttribute("socialMemberDto", socialMemberDto);
            return "social/memberUpdateForm"; // 에러가 있으면 폼으로 다시 리다이렉트
        }

        try {
            // Authentication 객체에서 이메일을 가져오기
            String email = ((CustomOAuth2User) authentication.getPrincipal()).getEmail();
            // 이메일을 사용하여 AbstractUser 객체 가져오기
            AbstractUser user = profileService.findByEmail(email);

            socialMemberService.updateSocialMember(user.getId(), socialMemberDto);

            return "redirect:/member/mypage";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("socialMemberDto", socialMemberDto);
            return "social/memberUpdateForm";
        }
    }

}
