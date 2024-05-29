package com.chujy.shopproject.repository;

import com.chujy.shopproject.constant.OrderStatus;
import com.chujy.shopproject.domain.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.user.email = :email order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Order o where o.user.email = :email")
    Long countOrder(@Param("email") String email);

    // 이메일로 주문 상태에 따라 주문 목록을 조회
    @Query("select o from Order o where o.user.email = :email and o.orderStatus = :status order by o.orderDate desc")
    List<Order> findOrdersByEmailAndOrderStatus(@Param("email") String email, @Param("status") OrderStatus status, Pageable pageable);

    // 이메일과 주문 상태로 총 주문건을 반환
    @Query("select count(o) from Order o where o.user.email = :email and o.orderStatus = :status")
    Long countOrderByEmailAndStatus(@Param("email") String email, @Param("status") OrderStatus status);

}
