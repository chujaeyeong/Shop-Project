package com.chujy.shopproject.oauth.domain;

import com.chujy.shopproject.domain.AbstractUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "social_member")
public class SocialMember extends AbstractUser {

    private String username;

}
