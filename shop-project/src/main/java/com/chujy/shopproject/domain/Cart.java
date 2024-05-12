package com.chujy.shopproject.domain;

import com.chujy.shopproject.oauth.domain.SocialMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)   // 단방향 매핑, ~ToOne 지연 로딩 설정
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_member_id")
    private SocialMember socialMember;


    // 일반 회원용 장바구니 생성
    public static Cart createMemberCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }

    // SNS 회원용 장바구니 생성
    public static Cart createSocialMemberCart(SocialMember socialMember) {
        Cart cart = new Cart();
        cart.setSocialMember(socialMember);
        return cart;
    }

}
