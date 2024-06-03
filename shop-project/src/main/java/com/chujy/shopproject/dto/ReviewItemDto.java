package com.chujy.shopproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewItemDto {

    private Long itemId;
    private String itemName;
    private LocalDateTime orderDate;
    private String imgUrl;
    private int price;
    private Long orderItemId;

}
