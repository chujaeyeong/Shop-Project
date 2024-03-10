package com.chujy.shopproject.repository;

import com.chujy.shopproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 중복 회원이 있는지 이메일로 검사하기 위한 쿼리메소드
    Member findByEmail(String email);

}
