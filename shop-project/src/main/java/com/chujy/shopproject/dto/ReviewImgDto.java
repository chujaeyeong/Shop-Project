package com.chujy.shopproject.dto;

import com.chujy.shopproject.domain.ReviewImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ReviewImgDto {

    private Long id;                // 리뷰 이미지 ID

    private String imgName;         // 저장된 이미지 파일명

    private String oriImgName;      // 원본 이미지 파일명

    private String imgUrl;          // 이미지 파일 접근 URL

    private static ModelMapper modelMapper = new ModelMapper();


    // ReviewImg 객체를 ReviewImgDto로 변환
    public static ReviewImgDto of(ReviewImg reviewImg) {
        return modelMapper.map(reviewImg, ReviewImgDto.class);
    }

}
