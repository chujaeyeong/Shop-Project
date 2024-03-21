package com.chujy.shopproject.repository;

import com.chujy.shopproject.domain.CartItem;
import com.chujy.shopproject.dto.CartDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // 장바구니 조회를 위한 JPQL
    @Query("select new com.chujy.shopproject.dto.CartDetailDto(ci.id, i.itemName, i.price, ci.count, im.imgUrl)" +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc")
    List<CartDetailDto> findCartDetailDtoList(Long cartId);

}
