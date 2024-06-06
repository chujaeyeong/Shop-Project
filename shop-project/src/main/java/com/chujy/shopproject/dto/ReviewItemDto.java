package com.chujy.shopproject.dto;

import com.chujy.shopproject.constant.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewItemDto {

    private Long reviewId;      // 리뷰 ID 필드 추가
    private Long itemId;
    private String itemName;
    private LocalDateTime orderDate;
    private String imgUrl;
    private int price;
    private Long orderItemId;
    private ReviewStatus reviewStatus;

}
