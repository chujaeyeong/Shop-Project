package com.chujy.shopproject.repository;

import com.chujy.shopproject.domain.SocialMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {

    Optional<SocialMember> findBySnsId(String snsId);
    Optional<SocialMember> findByEmail(String email);

}
