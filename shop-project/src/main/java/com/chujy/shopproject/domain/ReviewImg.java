package com.chujy.shopproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "review_img")
@Getter
@Setter
public class ReviewImg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private String imgName;     // 이미지 파일명

    private String oriImgName;  // 원본 이미지 파일명

    private String imgUrl;      // 이미지 조회 경로

    // 이미지 정보 업데이트 메소드
    public void updateReviewImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
