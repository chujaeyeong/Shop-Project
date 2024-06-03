package com.chujy.shopproject.dto;

import com.chujy.shopproject.domain.Review;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "평점은 필수 입력 값입니다.")
    private Integer rating;

    private List<ReviewImgDto> reviewImgDtoList;    // 리뷰 이미지 DTO 리스트

    private List<Long> reviewImgIds;                // 리뷰 이미지 아이디 리스트 (수정 시 사용)


    public static ReviewFormDto of(Review review) {
        ModelMapper modelMapper = new ModelMapper();
        ReviewFormDto dto = modelMapper.map(review, ReviewFormDto.class);
        dto.setItemId(review.getItem().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getName());
        return dto;
    }

}
