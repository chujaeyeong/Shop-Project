package com.chujy.shopproject.controller;

import com.chujy.shopproject.config.security.CustomUserDetails;
import com.chujy.shopproject.domain.AbstractUser;
import com.chujy.shopproject.domain.Member;
import com.chujy.shopproject.oauth.dto.CustomOAuth2User;
import com.chujy.shopproject.service.CartService;
import com.chujy.shopproject.service.MemberService;
import com.chujy.shopproject.service.OrderService;
import com.chujy.shopproject.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // 마이페이지 메인 이동
    @GetMapping("/member/mypage")
    public String myPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        AbstractUser user = null;
        if (principal instanceof CustomUserDetails) {
            Long userId = ((CustomUserDetails) principal).getId();
            user = profileService.findByUserId(userId);
        } else if (principal instanceof CustomOAuth2User) {
            String email = ((CustomOAuth2User) principal).getEmail();
            user = profileService.findByEmail(email);
        }

        if (user == null) {
            throw new IllegalStateException("Authenticated user not found");
        }

        model.addAttribute("user", user);
        return "member/mypage";
    }

}
