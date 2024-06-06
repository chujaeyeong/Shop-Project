package com.chujy.shopproject.service;

import com.chujy.shopproject.domain.Review;
import com.chujy.shopproject.domain.ReviewImg;
import com.chujy.shopproject.dto.ReviewImgDto;
import com.chujy.shopproject.repository.ReviewImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewImgService {

    @Value("${reviewImgLocation}")
    private String reviewImgLocation;

    private final ReviewImgRepository reviewImgRepository;
    private final FileService fileService;

    // 이미지 저장 로직
    public void saveReviewImg(Review review, List<MultipartFile> reviewImgFileList) throws Exception {
        for (MultipartFile reviewImgFile : reviewImgFileList) {
            if (!reviewImgFile.isEmpty()) {
                String oriImgName = reviewImgFile.getOriginalFilename();
                String imgName = fileService.uploadFile(reviewImgLocation, oriImgName, reviewImgFile.getBytes());
                String imgUrl = "/images/review/" + imgName;

                ReviewImg reviewImg = new ReviewImg();
                reviewImg.setReview(review);
                reviewImg.updateReviewImg(oriImgName, imgName, imgUrl);
                reviewImgRepository.save(reviewImg);
            }
        }
    }

    // 이미지 정보 업데이트
    public void updateReviewImg(Review review, List<MultipartFile> reviewImgFileList) throws Exception {
        // 기존 이미지를 모두 삭제
        deleteImagesByReviewId(review.getId());

        // 새로운 이미지 저장
        saveReviewImg(review, reviewImgFileList);
    }

    // Review 관련 이미지 전체 삭제
    public void deleteImagesByReviewId(Long reviewId) {
        List<ReviewImg> reviewImgs = reviewImgRepository.findByReviewId(reviewId);
        for (ReviewImg reviewImg : reviewImgs) {
            deleteImage(reviewImg);
        }
    }

    // 개별 이미지 삭제
    private void deleteImage(ReviewImg reviewImg) {
        if (!StringUtils.isEmpty(reviewImg.getImgName())) {
            try {
                fileService.deleteFile(reviewImgLocation + "/" + reviewImg.getImgName());
                reviewImgRepository.delete(reviewImg);
            } catch (Exception e) {
                log.error("Error deleting file: " + reviewImg.getImgName(), e);
            }
        }
    }

    // 리뷰 게시글의 이미지 리스트를 조회
    public List<ReviewImgDto> getReviewImages(Long reviewId) {
        List<ReviewImg> reviewImgs = reviewImgRepository.findByReviewId(reviewId);
        return reviewImgs.stream()
                .map(ReviewImgDto::of)
                .collect(Collectors.toList());
    }

}
