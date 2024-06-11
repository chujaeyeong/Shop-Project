package com.chujy.shopproject.dto;

import com.chujy.shopproject.domain.Member;
import com.chujy.shopproject.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    @ValidPassword(message = "비밀번호는 영문 대문자, 영문 소문자, 숫자 중 2개 이상을 포함하여 8자 이상이어야 합니다.")
    private String password;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;


    // 회원정보 수정을 위한 메소드
    public static MemberFormDto from(Member member) {
        MemberFormDto formDto = new MemberFormDto();
        formDto.setName(member.getName());
        formDto.setEmail(member.getEmail());
        formDto.setAddress(member.getAddress());

        return formDto;
    }

}
