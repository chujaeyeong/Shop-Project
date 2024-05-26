package com.chujy.shopproject.repository;

import com.chujy.shopproject.domain.AbstractUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AbstractUser, Long> {

    AbstractUser findByEmail(String email);

}
