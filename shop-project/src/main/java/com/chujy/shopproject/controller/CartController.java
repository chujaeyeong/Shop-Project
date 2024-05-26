package com.chujy.shopproject.controller;

import com.chujy.shopproject.dto.CartDetailDto;
import com.chujy.shopproject.dto.CartItemDto;
import com.chujy.shopproject.dto.CartOrderDto;
import com.chujy.shopproject.oauth.dto.CustomOAuth2User;
import com.chujy.shopproject.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 사용자의 유형에 맞게 principal.getName() 값을 다르게 반환하는 메소드
    private String getUserEmail(Principal principal) {
        if (principal instanceof CustomOAuth2User) {
            return ((CustomOAuth2User) principal).getEmail(); // 소셜 로그인 사용자의 경우 getName() 을 사용자 이름이 아닌 email 로 반환
        } else {
            return principal.getName(); // 일반 사용자인 경우 getName()은 이메일을 반환
        }
    }

    @PostMapping("/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto,
                                              BindingResult bindingResult,
                                              Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = getUserEmail(principal);
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public String orderHist(Principal principal, Model model) {

        String email = getUserEmail(principal);
        System.out.println("Logged in user email: " + email);  // 로그 추가

        List<CartDetailDto> cartDetailList = cartService.getCartList(email);
        model.addAttribute("cartItems", cartDetailList);

        return "cart/cartList";
    }
    
    @PatchMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                       int count, 
                                                       Principal principal) {
        
        if (count <= 0) {
            return new ResponseEntity<String>("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        }

        String email = getUserEmail(principal);
        if (!cartService.validateCartItem(cartItemId, email)) {
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCart(@PathVariable("cartItemId") Long cartItemId,
                                                   Principal principal) {

        String email = getUserEmail(principal);
        if (!cartService.validateCartItem(cartItemId, email)) {
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @PostMapping("/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto,
                                                      Principal principal) {

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
        String email = getUserEmail(principal);

        if (cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.", HttpStatus.FORBIDDEN);
        }

        for (CartOrderDto cartOrder : cartOrderDtoList) {
            if (!cartService.validateCartItem(cartOrder.getCartItemId(), email)) {
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

        Long orderId = cartService.orderCartItem(cartOrderDtoList, email);

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}
