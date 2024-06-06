package com.chujy.shopproject.dto;

import com.chujy.shopproject.domain.OrderItem;
import com.chujy.shopproject.domain.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.List;

@Getter
@Setter
public class ReviewFormDto {

    private Long id;                                // 후기 식별자

    private Long itemId;                            // 상품 식별자

    private Long userId;                            // 사용자 식별자

    private String userName;                        // 사용자 이름

    @NotBlank(message = "리뷰 내용은 필수 입력 값입니다.")
    private String content;

    @NotNull(message = "평점은 필수 입력 값입니다.")
    @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5 이하여야 합니다.")
    private Integer rating;

    private List<ReviewImgDto> reviewImgDtoList;    // 리뷰 이미지 DTO 리스트

    private List<Long> reviewImgIds;                // 리뷰 이미지 아이디 리스트 (수정 시 사용)

    private Long orderItemId;


    // Review 엔티티로부터 생성하는 메소드
    public static ReviewFormDto fromReview(Review review) {
        ModelMapper modelMapper = new ModelMapper();
        ReviewFormDto dto = modelMapper.map(review, ReviewFormDto.class);
        dto.setItemId(review.getItem().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getName());
        dto.setOrderItemId(review.getOrderItem().getId());
        return dto;
    }

    // OrderItem 엔티티로부터 생성하는 메소드
    public static ReviewFormDto fromOrderItem(OrderItem orderItem) {
        ModelMapper modelMapper = new ModelMapper();
        ReviewFormDto dto = new ReviewFormDto();
        dto.setItemId(orderItem.getItem().getId()); // 여기서 itemId가 설정됨
        dto.setUserId(orderItem.getOrder().getUser().getId());
        dto.setUserName(orderItem.getOrder().getUser().getName());
        dto.setOrderItemId(orderItem.getId());
        return dto;
    }

}
