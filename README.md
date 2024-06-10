# 쇼핑몰 개발 프로젝트 (Shop Application Project)
* 전자상거리의 주요 기능을 개발하는데 목표를 둔 쇼핑몰 프로젝트입니다.
* 사용자가 쉽게 제품을 탐색하고, 장바구니에 담고, 주문을 완료할 수 있도록 프로젝트를 설계했습니다.
* Spring Security, OAuth2, JPA, QueryDSL 기술을 학습하면서 다양한 기능을 개발해보는데 중점을 둔 프로젝트입니다.

<br>
<br>

## 1. 제작 기간
#### `2024년 3월 7일 ~ 2024년 6월 7일`

<br>
<br>

## 2. 사용 기술
### `Back-end`
* Java 17
* Spring Boot 3.2.3
* Spring Security 6
* JPA, Spring Data JPA
* Query DSL
* OAuth2
* Gradle
* MySQL 8.1.0
* H2 Database

### `Front-end`
* HTML
* Thymeleaf
* JavaScript

<br>
<br>

## 3. 주요 기능

<br>
<br>

## 4. ERD

<br>
<br>

## 5. API 명세서

<details>
<summary>🖥️ 쇼핑몰 메인 (Main)</summary>

#### 1) 메인페이지 조회
- **URL**: `/`
- **Method**: GET
- **Request Parameters**: `ItemSearchDto`, `Optional<Integer> page`, `Model model`
- **Description**: 메인 페이지를 조회합니다.

<br>

</details>

<details>
<summary>👤 일반 회원 (Member)</summary>

#### 1) 회원 가입 페이지 조회
- **URL**: `/members/new`
- **Method**: GET
- **Request Parameters**: `Model`
- **Description**: 회원 가입 페이지로 이동합니다.

#### 2) 회원 가입
- **URL**: `/members/new`
- **Method**: POST
- **Request Body**: `MemberFormDto`, `BindingResult`, `Model`
- **Description**: 새로운 회원을 가입시킵니다.

#### 3) 로그인 페이지 조회
- **URL**: `/members/login`
- **Method**: GET
- **Description**: 로그인 페이지를 조회합니다.

#### 4) 로그인 에러 페이지 조회
- **URL**: `/members/login/error`
- **Method**: GET
- **Request Parameters**: `Model`
- **Description**: 로그인 에러 페이지를 조회합니다.

#### 5) 회원 정보 수정 페이지 조회 - 일반회원
- **URL**: `/members/update`
- **Method**: GET
- **Request Parameters**: `Model`, `Authentication`
- **Description**: 회원 정보 수정 페이지를 조회합니다.

#### 6) 회원 정보 수정
- **URL**: `/members/update`
- **Method**: POST
- **Request Body**: `MemberFormDto`, `BindingResult`, `Authentication`, `RedirectAttributes`
- **Description**: 회원 정보를 수정합니다.

<br>

</details>

<details>
<summary>👥 SNS 회원 (OAuth, SocialMember)</summary>

#### 1) 구글 로그인 성공
- **URL**: `/login/oauth2/code/google`
- **Method**: GET
- **Request Parameters**: `OAuth2AuthenticationToken authentication`
- **Description**: 구글 로그인 성공 후 메인 페이지로 리다이렉트합니다.

#### 2) 네이버 로그인 성공
- **URL**: `/login/oauth2/code/naver`
- **Method**: GET
- **Request Parameters**: `OAuth2AuthenticationToken authentication`
- **Description**: 네이버 로그인 성공 후 메인 페이지로 리다이렉트합니다.

#### 3) 로그인 실패
- **URL**: `/loginFailure`
- **Method**: GET
- **Description**: 로그인 실패 후 에러 페이지로 리다이렉트합니다.

#### 4) SNS 회원 정보 수정 폼
- **URL**: `/social/member/updateForm`
- **Method**: GET
- **Request Parameters**: `Model`
- **Description**: 소셜 회원 정보 수정 폼 페이지로 이동합니다.

#### 5) 소셜 회원 정보 수정
- **URL**: `/social/member/update`
- **Method**: POST
- **Request Body**: `SocialMemberDto`, `BindingResult`, `Authentication`, `Model`
- **Description**: 소셜 회원 정보를 수정합니다.

<br>

</details>

<details>
<summary>👀 마이페이지 (Profile)</summary>

#### 1) 마이페이지 조회
- **URL**: `/member/mypage`
- **Method**: GET
- **Request Parameters**: `Model`
- **Description**: 마이페이지를 조회합니다.

#### 2) 프로필 수정 페이지 이동
- **URL**: `/member/edit`
- **Method**: GET
- **Request Parameters**: `Authentication`
- **Description**: 프로필 수정 페이지로 이동합니다.

<br>

</details>

<details>
<summary>👕 상품 (Item)</summary>

#### 1) 상품 등록 페이지 조회
- **URL**: `/admin/item/new`
- **Method**: GET
- **Request Parameters**: `Model`
- **Description**: 상품 등록 페이지를 조회합니다.

#### 2) 상품 등록
- **URL**: `/admin/item/new`
- **Method**: POST
- **Request Body**: `ItemFormDto`, `BindingResult`, `Model`, `List<MultipartFile>`
- **Description**: 새로운 상품을 등록합니다.

#### 3) 상품 상세 조회
- **URL**: `/admin/item/{itemId}`
- **Method**: GET
- **Request Parameters**: `Long itemId`, `Model`
- **Description**: 상품 상세 정보를 조회합니다.

#### 4) 상품 수정
- **URL**: `/admin/item/{itemId}`
- **Method**: POST
- **Request Body**: `ItemFormDto`, `BindingResult`, `Model`, `List<MultipartFile>`
- **Description**: 상품 정보를 수정합니다.

#### 5) 상품 관리 페이지 조회
- **URL**: `/admin/items`
- **Method**: GET
- **Request Parameters**: `ItemSearchDto`, `Optional<Integer> page`, `Model`
- **Description**: 상품 관리 페이지를 조회합니다.

#### 6) 상품 상세 페이지 조회
- **URL**: `/item/{itemId}`
- **Method**: GET
- **Request Parameters**: `Long itemId`, `Model`
- **Description**: 상품 상세 페이지를 조회합니다.

<br>

</details>

<details>
<summary>🛒 장바구니 (Cart)</summary>

#### 1) 장바구니에 상품 추가
- **URL**: `/cart`
- **Method**: POST
- **Request Body**: `CartItemDto`, `BindingResult`, `Principal`
- **Description**: 장바구니에 상품을 추가합니다.

#### 2) 장바구니 조회
- **URL**: `/cart`
- **Method**: GET
- **Request Parameters**: `Principal`, `Model`
- **Description**: 장바구니 목록을 조회합니다.

#### 3) 장바구니 아이템 수정
- **URL**: `/cartItem/{cartItemId}`
- **Method**: PATCH
- **Request Parameters**: `Long cartItemId`, `int count`, `Principal`
- **Description**: 장바구니 아이템 수량을 수정합니다.

#### 4) 장바구니 아이템 삭제
- **URL**: `/cartItem/{cartItemId}`
- **Method**: DELETE
- **Request Parameters**: `Long cartItemId`, `Principal`
- **Description**: 장바구니 아이템을 삭제합니다.

#### 5) 장바구니 아이템 주문
- **URL**: `/cart/orders`
- **Method**: POST
- **Request Body**: `CartOrderDto`, `Principal`
- **Description**: 장바구니 아이템을 주문합니다.

<br>

</details>

<details>
<summary>📝 주문 (Order)</summary>

#### 1) 주문 생성
- **URL**: `/order`
- **Method**: POST
- **Request Body**: `OrderDto`, `BindingResult`, `Principal`
- **Description**: 주문을 생성합니다.

#### 2) 주문 내역 조회
- **URL**: `/orders`, `/orders/{page}`
- **Method**: GET
- **Request Parameters**: `Optional<Integer> page`, `Principal`, `Model`
- **Description**: 주문 내역을 조회합니다.

#### 3) 주문 취소
- **URL**: `/order/{orderId}/cancel`
- **Method**: POST
- **Request Parameters**: `Long orderId`, `Principal`
- **Description**: 주문을 취소합니다.

<br>

</details>

<details>
<summary>✏️ 상품 리뷰 (Review)</summary>

#### 1) 리뷰 생성 폼 조회
- **URL**: `/reviews/new`
- **Method**: GET
- **Request Parameters**: `Long orderItemId`, `Model`
- **Description**: 리뷰 생성 폼 페이지로 이동합니다.

#### 2) 리뷰 생성
- **URL**: `/reviews/new`
- **Method**: POST
- **Request Body**: `ReviewFormDto`, `BindingResult`, `List<MultipartFile>`, `Long orderItemId`, `UserDetails`, `CustomOAuth2User`, `RedirectAttributes`, `Model`
- **Description**: 리뷰를 생성합니다.

#### 3) 리뷰 수정 폼 조회
- **URL**: `/reviews/{reviewId}/edit`
- **Method**: GET
- **Request Parameters**: `Long reviewId`, `Model`
- **Description**: 리뷰 수정 폼 페이지로 이동합니다.

#### 4) 리뷰 수정
- **URL**: `/reviews/{reviewId}/edit`
- **Method**: POST
- **Request Body**: `ReviewFormDto`, `BindingResult`, `List<MultipartFile>`, `Long reviewId`, `UserDetails`, `CustomOAuth2User`, `RedirectAttributes`, `Model`
- **Description**: 리뷰를 수정합니다.

#### 5) 리뷰 삭제
- **URL**: `/reviews/{reviewId}/delete`
- **Method**: POST
- **Request Parameters**: `Long reviewId`, `UserDetails`, `CustomOAuth2User`, `RedirectAttributes`
- **Description**: 리뷰를 삭제합니다.

#### 6) 리뷰 상세 조회
- **URL**: `/reviews/details/{reviewId}`
- **Method**: GET
- **Request Parameters**: `Long reviewId`, `Model`
- **Description**: 리뷰 상세 정보를 조회합니다.

#### 7) 리뷰 관리 페이지 조회
- **URL**: `/reviews/manage`
- **Method**: GET
- **Request Parameters**: `UserDetails`, `CustomOAuth2User`, `int page`, `Model`
- **Description**: 리뷰 관리 페이지를 조회합니다.

#### 8) 특정 상품의 리뷰 조회
- **URL**: `/reviews/item/{itemId}`
- **Method**: GET
- **Request Parameters**: `Long itemId`
- **Description**: 특정 상품의 리뷰를 조회합니다.

#### 9) 리뷰 상세 정보 조회
- **URL**: `/reviews/ajax/details/{reviewId}`
- **Method**: GET
- **Request Parameters**: `Long reviewId`
- **Description**: 리뷰의 상세 정보를 조회합니다.

<br>

</details>

<br>
<br>

## 6. 기능 구현

<br>
<br>

## 7. 기능별 작업 문서

<br>