package com.chujy.shopproject.repository;

import com.chujy.shopproject.domain.AbstractUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AbstractUser, Long> {

    Optional<AbstractUser> findByEmail(String email);
    boolean existsByEmail(String email);

}
