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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")   // 'member_id', 'social_member_id' 대신 'user_id' 사용
    private AbstractUser user;


    // 유저 (Member, SocialMember) 에 따른 장바구니 생성
    public static Cart createCart(AbstractUser user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cart;
    }

}
