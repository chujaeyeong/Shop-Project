package com.chujy.shopproject.service;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with ID: " + orderItemId));
        ReviewFormDto reviewFormDto = ReviewFormDto.fromOrderItem(orderItem);

        return reviewFormDto;
    }

    // orderItemId 조회 메소드
    public OrderItem findOrderItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
    }

    // 리뷰 생성
    public Long saveReview(ReviewFormDto reviewFormDto, List<MultipartFile> reviewImgFileList) throws Exception {
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
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));

        List<ReviewImgDto> reviewImgDtos = reviewImgService.getReviewImages(reviewId);
        ReviewFormDto reviewFormDto = ReviewFormDto.fromReview(review);
        reviewFormDto.setReviewImgDtoList(reviewImgDtos);
        return reviewFormDto;
    }

    // 리뷰 수정
    public void updateReview(ReviewFormDto reviewFormDto, List<MultipartFile> reviewImgFileList) throws Exception {
        Review review = reviewRepository.findById(reviewFormDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewFormDto.getId()));

        review.updateReview(reviewFormDto);
        reviewRepository.save(review); // 수정된 리뷰를 저장

        // 이미지 업데이트
        if (reviewImgFileList != null && !reviewImgFileList.isEmpty()) {
            reviewImgService.updateReviewImg(review, reviewImgFileList);
        }

        // 수정 후 리뷰 데이터 확인
        Review updatedReview = reviewRepository.findById(reviewFormDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Updated review not found with ID: " + reviewFormDto.getId()));
        log.info("Updated Review Content: " + updatedReview.getContent());
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));
        reviewImgService.deleteImagesByReviewId(reviewId); // ReviewImgService를 사용하여 이미지 삭제

        // 리뷰 작성 상태 업데이트
        OrderItem orderItem = review.getOrderItem();
        if (orderItem != null) {
            orderItem.setReviewStatus(ReviewStatus.NOT_REVIEWED);
            orderItem.setReview(null); // OrderItem과의 연관관계 해제
            orderItemRepository.save(orderItem);
        }

        reviewRepository.delete(review);

        // 리뷰가 실제로 삭제되었는지 확인
        if (reviewRepository.findById(reviewId).isPresent()) {
            throw new IllegalStateException("리뷰 삭제에 실패했습니다.");
        } else {
            log.info("Review successfully deleted");
        }
    }

    // 사용자가 리뷰할 수 있는 상품 조회 메소드
    public Page<ReviewItemDto> getItemsEligibleForReview(Long userId, Pageable pageable) {
        List<Order> orders = orderService.findOrdersByUserId(userId);

        List<ReviewItemDto> reviewItemDtos = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(orderItem -> orderItem.getReviewStatus() == ReviewStatus.NOT_REVIEWED || orderItem.getReviewStatus() == ReviewStatus.REVIEWED)
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
                    if (orderItem.getReview() != null) {
                        dto.setReviewId(orderItem.getReview().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviewItemDtos.size());
        return new PageImpl<>(reviewItemDtos.subList(start, end), pageable, reviewItemDtos.size());
    }

}
