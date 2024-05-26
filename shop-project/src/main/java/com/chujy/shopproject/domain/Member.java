package com.chujy.shopproject.domain;

import com.chujy.shopproject.constant.Role;
import com.chujy.shopproject.dto.MemberFormDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member extends AbstractUser {

    private String password;

    // Member 생성 메소드를 만들어서 관리
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());

        // BCryptPasswordEncoder Bean 을 파라미터로 넘겨서 패스워드 암호화
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);

        member.setRole(Role.ROLE_USER);

        return member;
    }

}
