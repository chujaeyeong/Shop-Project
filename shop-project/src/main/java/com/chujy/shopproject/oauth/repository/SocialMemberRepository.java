package com.chujy.shopproject.oauth.repository;

import com.chujy.shopproject.oauth.domain.SocialMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {

    SocialMember findByUsername(String username);

}
