package com.chujy.shopproject.service;

import com.chujy.shopproject.constant.OrderStatus;
import com.chujy.shopproject.constant.ReviewStatus;
import com.chujy.shopproject.domain.*;
import com.chujy.shopproject.dto.ReviewFormDto;
import com.chujy.shopproject.dto.ReviewImgDto;
import com.chujy.shopproject.dto.ReviewItemDto;
import com.chujy.shopproject.repository.ItemRepository;
import com.chujy.shopproject.repository.OrderItemRepository;
import com.chujy.shopproject.repository.ReviewRepository;
import com.chujy.shopproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final OrderItemRepository orderItemRepository;
    private final ReviewImgService reviewImgService;


    // 리뷰 생성 폼 DTO 생성
    public ReviewFormDto createReviewFormDto(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
        return ReviewFormDto.fromOrderItem(orderItem);
    }

    // 리뷰 생성
    public Long saveReview(ReviewFormDto reviewFormDto, List<MultipartFile> reviewImgFileList) throws Exception {
        // 상품 및 사용자 구매 이력 검증
        Item item = itemRepository.findById(reviewFormDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        OrderItem orderItem = orderItemRepository.findById(reviewFormDto.getOrderItemId()).orElseThrow(EntityNotFoundException::new);

        if (!orderService.hasPurchased(reviewFormDto.getUserId(), reviewFormDto.getItemId())) {
            throw new IllegalStateException("리뷰 작성 권한이 없습니다.");
        }

        // 이미 리뷰가 작성된 경우 예외 처리
        if (orderItem.getReviewStatus() == ReviewStatus.REVIEWED) {
            throw new IllegalStateException("이미 리뷰가 작성된 주문 상품입니다.");
        }

        // 리뷰 생성
        AbstractUser user = userRepository.findById(reviewFormDto.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. : " + reviewFormDto.getUserId()));
        Review review = Review.createReview(reviewFormDto, item, user, orderItem);
        reviewRepository.save(review);

        // 이미지 등록
        reviewImgService.saveReviewImg(review, reviewImgFileList);

        // 리뷰 상태 업데이트
        orderItem.setReviewStatus(ReviewStatus.REVIEWED);
        orderItemRepository.save(orderItem);

        return review.getId();
    }

    // 리뷰 조회
    public ReviewFormDto getReviewDetails(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(EntityNotFoundException::new);
        List<ReviewImgDto> reviewImgDtos = reviewImgService.getReviewImages(reviewId);

        ReviewFormDto reviewFormDto = ReviewFormDto.fromReview(review);
        reviewFormDto.setReviewImgDtoList(reviewImgDtos);
        return reviewFormDto;
    }

    // 리뷰 수정
    public void updateReview(ReviewFormDto reviewFormDto, List<MultipartFile> reviewImgFileList) throws Exception {
        Review review = reviewRepository.findById(reviewFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        review.updateReview(reviewFormDto);
        reviewImgService.updateReviewImg(review, reviewImgFileList);
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(EntityNotFoundException::new);
        reviewImgService.deleteImagesByReviewId(reviewId); // ReviewImgService를 사용하여 이미지 삭제

        // 리뷰 작성 상태 업데이트
        OrderItem orderItem = review.getOrderItem();
        if (orderItem != null) {
            orderItem.setReviewStatus(ReviewStatus.NOT_REVIEWED);
            orderItemRepository.save(orderItem);
        }

        reviewRepository.delete(review);
    }

    // 사용자가 리뷰할 수 있는 상품 조회 메소드
    public Page<ReviewItemDto> getItemsEligibleForReview(Long userId, Pageable pageable) {
        List<Order> orders = orderService.findOrdersByUserId(userId);

        List<ReviewItemDto> reviewItemDtos = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(orderItem -> orderItem.getReviewStatus() == ReviewStatus.NOT_REVIEWED)  // 리뷰되지 않은 상품만 필터링
                .map(orderItem -> {
                    ReviewItemDto dto = new ReviewItemDto();
                    dto.setOrderItemId(orderItem.getId());
                    dto.setItemId(orderItem.getItem().getId());
                    dto.setItemName(orderItem.getItem().getItemName());
                    dto.setOrderDate(orderItem.getOrder().getOrderDate());

                    // imgUrl 설정
                    ItemImg itemImg = itemImgService.getMainImageForItem(orderItem.getItem().getId());
                    if (itemImg != null) {
                        dto.setImgUrl(itemImg.getImgUrl());
                    }

                    dto.setPrice(orderItem.getOrderPrice());
                    dto.setReviewStatus(orderItem.getReviewStatus());
                    return dto;
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviewItemDtos.size());
        return new PageImpl<>(reviewItemDtos.subList(start, end), pageable, reviewItemDtos.size());
    }

}
