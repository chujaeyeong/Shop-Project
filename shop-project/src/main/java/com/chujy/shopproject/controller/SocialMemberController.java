package com.chujy.shopproject.controller;

import com.chujy.shopproject.dto.SocialMemberDto;
import com.chujy.shopproject.service.SocialMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class SocialMemberController {

    private final SocialMemberService socialMemberService;

    @GetMapping("/success")
    public String socialMemberForm(Model model) {
        model.addAttribute("socialMemberDto", new SocialMemberDto());
        return "member/memberForm";
    }

}
