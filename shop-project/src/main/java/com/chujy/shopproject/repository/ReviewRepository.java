package com.chujy.shopproject.repository;

import com.chujy.shopproject.constant.OrderStatus;
import com.chujy.shopproject.domain.Order;
import com.chujy.shopproject.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByItemId(Long itemId);

    @Query("SELECT r.item.id FROM Review r WHERE r.user.id = :userId")
    Set<Long> findReviewedItemIdsByUserId(Long userId);

    @Query("SELECT o FROM Order o JOIN o.orderItems oi WHERE o.user.id = :userId AND o.orderStatus = :status AND oi.item.id NOT IN (SELECT r.item.id FROM Review r WHERE r.user.id = :userId)")
    Page<Order> findOrdersWithItemsEligibleForReview(@Param("userId") Long userId, @Param("status") OrderStatus status, Pageable pageable);

}
