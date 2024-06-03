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

    // 사용자의 ID 와 상품 ID, 주문 상태 (ORDER) 을 만족하는 주문의 여부를 확인 후 true / false 로 반환 (Review 관련 비즈니스 로직에서 사용할 것)
    @Query("select count(o) > 0 from Order o join o.orderItems oi where o.user.id = :userId and oi.item.id = :itemId and o.orderStatus = :status")
    boolean existsByUserIdAndItemIdAndOrderStatus(@Param("userId") Long userId, @Param("itemId") Long itemId, @Param("status") OrderStatus status);

    // 사용자 ID와 주문 상태로 주문을 조회
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus = :status ORDER BY o.orderDate DESC")
    List<Order> findByUserIdAndOrderStatus(@Param("userId") Long userId, @Param("status") OrderStatus status);

    // 사용자 ID와 상품 ID로 주문을 조회하여 다중 결과를 반환
    @Query("SELECT o FROM Order o JOIN o.orderItems oi WHERE o.user.id = :userId AND oi.item.id = :itemId AND o.orderStatus = :status")
    List<Order> findOrdersContainingItem(@Param("userId") Long userId, @Param("itemId") Long itemId, @Param("status") OrderStatus status);

}
