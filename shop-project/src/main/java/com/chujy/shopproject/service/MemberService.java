package com.chujy.shopproject.service;

import com.chujy.shopproject.config.security.CustomUserDetails;
import com.chujy.shopproject.domain.Member;
import com.chujy.shopproject.dto.MemberFormDto;
import com.chujy.shopproject.repository.MemberRepository;
import com.chujy.shopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    // 이미 가입한 회원일 경우 예외 발생
    private void validateDuplicateMember(Member member) {
        if (userRepository.existsByEmail(member.getEmail())) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return new CustomUserDetails(
                member.getEmail(),
                member.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name())),
                member.getId()
        );
    }

    // ID로 회원 조회 - 일반회원
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

    // 회원정보 수정
    public Member updateMember(Long id, MemberFormDto memberFormDto) {
        Member member = findById(id);
        if (member == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        if (memberFormDto.getName() != null) {
            member.setName(memberFormDto.getName());
        }
        if (memberFormDto.getPassword() != null && !memberFormDto.getPassword().isEmpty()) {
            // 현재 비밀번호와 일치하는지 확인 후 업데이트
            member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
        }
        if (memberFormDto.getAddress() != null) {
            member.setAddress(memberFormDto.getAddress());
        }

        return member;
    }

}
