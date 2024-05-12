package com.chujy.shopproject.oauth.domain;

import com.chujy.shopproject.constant.Role;
import com.chujy.shopproject.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "social_member")
public class SocialMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "social_member_generator")
    @TableGenerator(
            name = "social_member_generator",
            table = "social_member_id_gen",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            pkColumnValue = "SocialMember_ID",
            allocationSize = 1
    )
    private Long id;

    private String username;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

}
