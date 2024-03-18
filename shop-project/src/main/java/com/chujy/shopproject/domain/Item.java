package com.chujy.shopproject.domain;

import com.chujy.shopproject.constant.ItemSellStatus;
import com.chujy.shopproject.dto.ItemFormDto;
import com.chujy.shopproject.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;    // 상품코드

    @Column(nullable = false, length = 50)
    private String itemName;    // 상품명

    @Column(name = "price", nullable = false)
    private int price;  // 기격

    @Column(nullable = false)
    private int stockNumber;    // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태


    // 상품 변경 메소드를 만들어서 관리
    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    // 상품의 재고를 감소시키는 로직
    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부적합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    // 주문 취소 시 상품 재고 증가를 위한 메소드
    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }

}
