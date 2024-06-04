package com.chujy.shopproject.domain;

import com.chujy.shopproject.dto.ReviewFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "review")
@Getter
@Setter
@ToString
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_id")
    private Long id;    // 리뷰 게시물 코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AbstractUser user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    private String content;

    private Integer rating;


    // 리뷰 생성 메소드를 만들어서 관리
    public static Review createReview(ReviewFormDto reviewFormDto, Item item, AbstractUser user, OrderItem orderItem) {
        Review review = new Review();
        review.setItem(item);
        review.setUser(user);
        review.setOrderItem(orderItem);
        review.setContent(reviewFormDto.getContent());
        review.setRating(reviewFormDto.getRating());

        return review;
    }

    // 리뷰 수정 시 사용할 업데이트 메소드
    public void updateReview(ReviewFormDto reviewFormDto) {
        this.content = reviewFormDto.getContent();
        this.rating = reviewFormDto.getRating();
    }

}
