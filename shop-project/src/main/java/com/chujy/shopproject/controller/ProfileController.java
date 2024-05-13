package com.chujy.shopproject.controller;

import com.chujy.shopproject.config.security.CustomUserDetails;
import com.chujy.shopproject.domain.Member;
import com.chujy.shopproject.service.CartService;
import com.chujy.shopproject.service.MemberService;
import com.chujy.shopproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final MemberService memberService;
    private final CartService cartService;
    private final OrderService orderService;

    // 마이페이지 메인 이동
    @GetMapping("/member/mypage")
    public String myPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();  // 사용자 고유 ID 추출
            Member member = memberService.findById(userId);  // ID로 회원 정보 조회
            model.addAttribute("member", member);  // 모델에 회원 정보 추가
            return "member/mypage";
        }
        throw new IllegalStateException("Authenticated user not found");
    }

}
