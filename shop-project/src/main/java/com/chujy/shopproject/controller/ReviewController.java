package com.chujy.shopproject.controller;

import com.chujy.shopproject.domain.AbstractUser;
import com.chujy.shopproject.domain.Item;
import com.chujy.shopproject.dto.ReviewFormDto;
import com.chujy.shopproject.dto.ReviewItemDto;
import com.chujy.shopproject.service.ReviewService;
import com.chujy.shopproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    // 리뷰 관리에서 사용할 페이지 변수
    private static final int PAGE_SIZE = 5;
    private static final int MAX_PAGE = 5;

    // 리뷰 생성 폼 페이지로 이동
    @GetMapping("/new")
    public String createReviewForm(Model model) {
        ReviewFormDto reviewFormDto = new ReviewFormDto();
        model.addAttribute("reviewFormDto", reviewFormDto);
        return "review/reviewForm";
    }

    // 리뷰 생성
    @PostMapping("/new")
    public String createReview(@ModelAttribute @Valid ReviewFormDto reviewFormDto,
                               BindingResult bindingResult,
                               @RequestParam("reviewImages") List<MultipartFile> reviewImages,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "review/reviewForm";  // 입력 오류가 있을 경우 다시 폼을 보여줌
        }
        try {
            AbstractUser user = userService.getUserByEmail(userDetails.getUsername());
            reviewFormDto.setUserId(user.getId());
            Long reviewId = reviewService.saveReview(reviewFormDto, reviewImages);
            return "redirect:/reviews/details/" + reviewId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "리뷰 생성 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/reviews/new";
        }
    }

    // 리뷰 수정 폼 페이지로 이동
    @GetMapping("/{reviewId}/edit")
    public String updateReviewForm(@PathVariable Long reviewId, Model model) {
        ReviewFormDto reviewDto = reviewService.getReviewDetails(reviewId);
        model.addAttribute("reviewFormDto", reviewDto);
        return "reviews/editReviewForm";
    }

    // 리뷰 수정
    @PostMapping("/{reviewId}/edit")
    public String updateReview(@PathVariable Long reviewId,
                               @ModelAttribute @Valid ReviewFormDto reviewFormDto,
                               BindingResult bindingResult,
                               @RequestParam("reviewImages") List<MultipartFile> reviewImages,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("reviewFormDto", reviewFormDto);
            return "reviews/editReviewForm";  // 입력 오류가 있을 경우 다시 폼을 보여줌
        }
        try {
            AbstractUser user = userService.getUserByEmail(userDetails.getUsername());
            if (!user.getId().equals(reviewFormDto.getUserId())) {
                throw new IllegalStateException("Unauthorized attempt to update review");
            }
            reviewService.updateReview(reviewFormDto, reviewImages);
            return "redirect:/reviews/details/" + reviewId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "리뷰 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/reviews/" + reviewId + "/edit";
        }
    }

    // 리뷰 삭제
    @PostMapping("/{reviewId}/delete")
    public String deleteReview(@PathVariable Long reviewId,
                               @AuthenticationPrincipal UserDetails userDetails) {
        ReviewFormDto review = reviewService.getReviewDetails(reviewId);
        AbstractUser user = userService.getUserByEmail(userDetails.getUsername());
        if (!user.getId().equals(review.getUserId())) {
            throw new IllegalStateException("Unauthorized attempt to delete review");
        }
        reviewService.deleteReview(reviewId);
        return "redirect:/reviews/list";
    }

    // 리뷰 상세 정보 페이지로 이동
    @GetMapping("/details/{reviewId}")
    public String showReviewDetails(@PathVariable Long reviewId, Model model) {
        ReviewFormDto reviewDto = reviewService.getReviewDetails(reviewId);
        model.addAttribute("reviewFormDto", reviewDto);
        return "reviews/reviewDetails";
    }

    // 리뷰 관리 페이지 표시
    @GetMapping("/manage")
    public String manageReviews(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {
        AbstractUser user = userService.getUserByEmail(userDetails.getUsername());
        Pageable pageable = PageRequest.of(page, PAGE_SIZE); // 한 페이지에 5개씩 표시
        Page<ReviewItemDto> itemsEligibleForReview = reviewService.getItemsEligibleForReview(user.getId(), pageable);
        model.addAttribute("itemsEligibleForReview", itemsEligibleForReview);
        model.addAttribute("maxPage", MAX_PAGE);
        return "reviews/manageReviews";
    }

}
