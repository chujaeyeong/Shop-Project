package com.chujy.shopproject.service;

import com.chujy.shopproject.domain.Item;
import com.chujy.shopproject.domain.Member;
import com.chujy.shopproject.domain.Order;
import com.chujy.shopproject.domain.OrderItem;
import com.chujy.shopproject.dto.OrderDto;
import com.chujy.shopproject.repository.ItemRepository;
import com.chujy.shopproject.repository.MemberRepository;
import com.chujy.shopproject.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

}
