package com.chujy.shopproject.repository;

import com.chujy.shopproject.domain.SocialMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {

    SocialMember findBySnsId(String snsId);

}
