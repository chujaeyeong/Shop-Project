package com.chujy.shopproject.service;

import com.chujy.shopproject.constant.OrderStatus;
import com.chujy.shopproject.domain.*;
import com.chujy.shopproject.dto.OrderDto;
import com.chujy.shopproject.dto.OrderHistDto;
import com.chujy.shopproject.dto.OrderItemDto;
import com.chujy.shopproject.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        AbstractUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다."));

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(user, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        AbstractUser curUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다."));
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        AbstractUser savedUser = order.getUser();

        if (!StringUtils.equals(curUser.getEmail(), savedUser.getEmail())) {
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    public Long orders(List<OrderDto> orderDtoList, String email) {
        AbstractUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다."));
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(user, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    // 주문 상태가 ORDER 인 주문의 총 금액을 계산하는 메소드
    @Transactional(readOnly = true)
    public int getTotalOrderAmount(String email) {
        // 주문 상태가 ORDER인 주문만 조회
        List<Order> orders = orderRepository.findOrdersByEmailAndOrderStatus(email, OrderStatus.ORDER, Pageable.unpaged());

        // 주문 상태가 ORDER인 주문들의 총 금액 계산
        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        return totalAmount;
    }

    // 사용자가 특정 상품에 대한 구매 이력이 있는지 확인하는 로직 (Review 비즈니스 로직에서 사용할 것)
    @Transactional(readOnly = true)
    public boolean hasPurchased(Long userId, Long itemId) {
        return orderRepository.existsByUserIdAndItemIdAndOrderStatus(userId, itemId, OrderStatus.ORDER);
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.ORDER);
    }

    // 사용자 ID와 상품 ID로 특정 주문을 조회
    @Transactional(readOnly = true)
    public Order findOrderContainingItem(Long userId, Long itemId) {
        List<Order> orders = orderRepository.findOrdersContainingItem(userId, itemId, OrderStatus.ORDER);
        if (orders.isEmpty()) {
            return null;  // 결과가 없는 경우
        }
        return orders.get(0);  // 첫 번째 결과 반환
    }

}
