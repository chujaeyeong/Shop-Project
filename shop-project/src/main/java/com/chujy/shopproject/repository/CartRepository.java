package com.chujy.shopproject.repository;

import com.chujy.shopproject.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
