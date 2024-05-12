package com.chujy.shopproject.domain;

import com.chujy.shopproject.constant.Role;
import com.chujy.shopproject.dto.SocialMemberDto;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("SocialMember")
@Table(name = "social_member")
@Getter
@Setter
public class SocialMember extends Member {

    @Column(unique = true)
    private String snsId;

    // SocialMember 생성 메소드를 만들어서 관리
    public static SocialMember createSocialMember(SocialMemberDto socialMemberDto) {
        SocialMember socialMember = new SocialMember();
        socialMember.setEmail(socialMemberDto.getEmail());
        socialMember.setName(socialMemberDto.getName());
        socialMember.setSnsId(socialMemberDto.getSnsId());
        socialMember.setAddress(socialMemberDto.getAddress());
        socialMember.setRole(Role.USER);

        return socialMember;
    }

}
