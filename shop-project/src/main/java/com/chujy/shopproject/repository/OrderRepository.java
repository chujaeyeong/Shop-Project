package com.chujy.shopproject.repository;

import com.chujy.shopproject.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
