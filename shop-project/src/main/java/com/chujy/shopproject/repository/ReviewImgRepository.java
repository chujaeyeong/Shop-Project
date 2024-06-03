package com.chujy.shopproject.repository;

import com.chujy.shopproject.domain.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {

    List<ReviewImg> findByReviewId(Long reviewId);

}
