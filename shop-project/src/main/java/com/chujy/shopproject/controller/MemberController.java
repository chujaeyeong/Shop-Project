package com.chujy.shopproject.controller;

import com.chujy.shopproject.domain.Member;
import com.chujy.shopproject.dto.MemberFormDto;
import com.chujy.shopproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping("/new")
    public String memberForm(MemberFormDto memberFormDto) {
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        memberService.saveMember(member);

        return "redirect:/";
    }

}
