package com.chujy.shopproject.controller;

import com.chujy.shopproject.config.security.CustomUserDetails;
import com.chujy.shopproject.domain.Member;
import com.chujy.shopproject.dto.MemberFormDto;
import com.chujy.shopproject.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        // 에러가 있다면 회원가입 페이지로 이동
        if (bindingResult.hasErrors()){
            return "member/memberForm";
        }

        // 에러가 없으면 회원 저장
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }

        // 회원가입 시 중복 회원 가입 에러 발생하면 에러 메시지를 뷰로 전달
        catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    // 회원정보 수정 페이지 이동
    @GetMapping("/update")
    public String showUpdateForm(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        Member member = memberService.findById(userId);
        model.addAttribute("memberFormDto", MemberFormDto.from(member));
        return "member/memberUpdateForm";
    }

    // 회원정보 수정
    @PostMapping("/update")
    public String updateMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @Valid MemberFormDto memberFormDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "/member/updateMemberForm";
        }

        try {
            memberService.updateMember(userDetails.getId(), memberFormDto);
            redirectAttributes.addFlashAttribute("message", "회원정보가 수정되었습니다!");
            return "redirect:/member/mypage";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "회원정보 수정 실패: " + e.getMessage());
            return "/member/updateMemberForm";
        }
    }

}
