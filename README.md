# 쇼핑몰 개발 프로젝트 (Shop Application Project)
* 전자상거리의 주요 기능을 개발하는데 목표를 둔 쇼핑몰 프로젝트입니다.
* 사용자가 쉽게 제품을 탐색하고, 장바구니에 담고, 주문을 완료할 수 있도록 프로젝트를 설계했습니다.
* 서비스의 전반적인 기능을 설계, 개발하면서 다양한 기능을 개발하는데 목적을 두었습니다. 
* Spring Security, OAuth2, JPA, QueryDSL 기술을 학습하고, 다양한 기능을 개발하는데 신규 학습 기술을 프로젝트에 적용했습니다.

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
* #### 👤 `사용자`
    * 스프링 시큐리티를 이용해 폼 입력 형식의 회원가입을 할 수 있습니다.
    * 스프링 시큐리티를 이용해 로그인 및 로그아웃을 할 수 있습니다.
    * 회원가입 및 로그인 시 폼 입력 형식으로 가입한 일반회원의 경우, 비밀번호 설정 형식에 맞춰 비밀번호를 입력하도록 검증 절차를 추가했습니다.
    * OAuth2.0 방식을 사용하여 네이버, 구글 계정으로 로그인을 할 수 있습니다.
    * 회원의 역할에 따라 (ROLE_ADMIN, ROLE_USER) 페이지별 접근 권한을 부여했습니다.
    * 마이페이지에서 회원 접근 기능으로 이동하고, 회원 정보를 수정할 수 있습니다.

* #### 👕 `상품`
    * 관리자 (ROLE_ADMIN) 계정으로 로그인 후 상품 등록을 할 수 있습니다.
    * 관리자 계정으로 상품 리스트 및 각 상품의 상세 정보를 조회할 수 있습니다.
    * 관리자 계정으로 상품 정보를 수정할 수 있습니다.
    * 메인 페이지 및 상품 상세 정보를 비회원 상태에서도 조회할 수 있습니다.
    * 메인페이지의 검색창으로 상품명을 검색할 수 있습니다.

* #### 🛒 `장바구니`
    * 로그인 후 상품 상세 페이지에서 수량을 선택하여 장바구니에 상품을 담을 수 있습니다.
    * 장바구니에 상품 수량을 수정할 수 있습니다.
    * 장바구니에 담은 상품을 삭제할 수 있습니다.
    * 장바구니에 담은 상품 중 원하는 상품을 선택하여 주문할 수 있습니다.

* #### 📝 `주문`
    * 로그인 후 상품 상세 페이지에서 수량을 선택하여 주문할 수 있습니다.
    * 구매이력 페이지에서 주문 내역을 조회할 수 있습니다.
    * 주문했던 상품을 취소할 수 있습니다.

* #### ✏️ `상품 리뷰`
    * 마이페이지에서 리뷰 관리 페이지로 이동할 수 있습니다.
    * 리뷰 관리 페이지에서 주문한 상품의 리뷰를 관리할 수 있습니다.
    * 주문한 상품의 리뷰를 작성할 수 있습니다.
    * 작성한 리뷰를 수정할 수 있습니다.
    * 작성한 리뷰를 삭제할 수 있습니다.
    * 비회원 상태에서 상품의 상세 정보 페이지의 상품의 리뷰 리스트 및 리뷰 상세 정보를 조회할 수 있습니다.

<br>
<br>

## 4. ERD

<img width="900" alt="Shop-Project ERD" src="https://github.com/chujaeyeong/Shop-Project/assets/123634960/d4f924aa-d866-42e7-9b2b-a26d67978273">

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

## 7. 기능별 작업 코드 바로가기
* #### `회원가입`
    - [회원가입 서비스 계층 구현 및 테스트](https://github.com/chujaeyeong/Shop-Project/commit/3d0701c5452cd8db5b9a039d168ee6582853b11d)
    - [회원가입 웹 계층 구현](https://github.com/chujaeyeong/Shop-Project/commit/ebd6154cb8d434341c24910f597efd9fb9ee0830)
    - [회원가입 검증 추가](https://github.com/chujaeyeong/Shop-Project/commit/497a4bda0f409e3cb02e14b92acb2b0ff59161ae)
    - [회원가입 및 로그인 시 비밀번호 보기 토글 추가](https://github.com/chujaeyeong/Shop-Project/commit/7b5f93650a1ec92eaf5df86eee7977ce7b1439c9)
    - [회원가입 시 중복 가입 검증 로직 수정](https://github.com/chujaeyeong/Shop-Project/commit/66ed506658e7ce3471d2c204c7f5b0b8f235874f)
    - [커스텀 어노테이션으로 비밀번호 검증 추가](https://github.com/chujaeyeong/Shop-Project/commit/e65bf5d7055ae2cd7f52aa0f44cd197d4de70436)

* #### `로그인 & 로그아웃`
    - [로그인, 로그아웃 구현 및 테스트](https://github.com/chujaeyeong/Shop-Project/commit/9c053d7d096492f8a380b6c0e08adeb4191e3ad5)
    
* #### `SNS 계정 연동 로그인`
    - [OAuth2.0 방식의 네이버, 구글 연동 로그인 구현](https://github.com/chujaeyeong/Shop-Project/commit/c5f8d719d2b4005274ba18250051aa1bfa453870)
    - [OAuth2 SNS 로그인 로직 수정](https://github.com/chujaeyeong/Shop-Project/commit/f242e3fe2e23989f9a5e55ac1a9eee654a5c56ae)

* #### `상품`
    - [상품 도메인 추가 및 상품 save 기능 추가, 테스트 진행](https://github.com/chujaeyeong/Shop-Project/commit/612560b883017f6f8990dd7daa46ec37ceef4398)
    - [상품 조회 관련 메소드 추가 및 테스트](https://github.com/chujaeyeong/Shop-Project/commit/6bd9ce81b607270484a826e478036b7732f2b05e)
    - [상품 검색에 사용할 QueryDSL 설정 및 테스트](https://github.com/chujaeyeong/Shop-Project/commit/99a13a820a4e00fe9e2d8b4f4a8d5a4f9b345217)
    - [상품 리포지토리 계층에 QueryDslPredicateExcutor 인터페이스 상속 추가](https://github.com/chujaeyeong/Shop-Project/commit/7be90cbe7add4f92ee9551e8bde606aaafdf2d2b)
    - [상품 관리 페이지 권한 설정](https://github.com/chujaeyeong/Shop-Project/commit/6b7bf373082ac05fbd77c3c89ed533cdb5a926e7)
    - [상품 등록 기능 구현 및 테스트](https://github.com/chujaeyeong/Shop-Project/commit/d53a7c4d55a81da2d6e6586b78646e18b8e59f9f)
    - [등록 상품 리스트 조회 후 상품 검색, 관리 기능 구현](https://github.com/chujaeyeong/Shop-Project/commit/8b68d2b81d6fdddde35e4a7c7b817ea75d12dafd)
    - [메인 화면에 상품 리스트 노출 구현](https://github.com/chujaeyeong/Shop-Project/commit/84f78c9b4e120e1c4d9b623f066760fe2e63c3fe)
    - [상품 상세 페이지 구현](https://github.com/chujaeyeong/Shop-Project/commit/7c84423414a2e922873f4cb9a8b42a540ece50f0)
    - [상품 재고 변경 시 상품 상태를 업데이트](https://github.com/chujaeyeong/Shop-Project/commit/bf0f95d59b3fef8d82dd84a59058382496897288)

* #### `주문`
    - [장바구니 아이템 주문, 주문 상품 엔티티 생성](https://github.com/chujaeyeong/Shop-Project/commit/880fe8fe0235f86954db4373e8bfd80b1eacaa09)
    - [주문 관련 엔티티 영속성 전이 옵션 추가 및 테스트](https://github.com/chujaeyeong/Shop-Project/commit/4205b21486119f22e78d5a529ea050be8d1b6e6f)
    - [지연로딩 테스트](https://github.com/chujaeyeong/Shop-Project/commit/4205b21486119f22e78d5a529ea050be8d1b6e6f)
    - [주문 기능 구현 및 테스트](https://github.com/chujaeyeong/Shop-Project/commit/98591acb3aebc2e9b178471b953a2895075fbbe4)
    - [회원 주문 이력 조회 기능 구현](https://github.com/chujaeyeong/Shop-Project/commit/386eec1792d45225192fea84b772b68b18aa5a58)
    - [회원 주문 취소 기능 구현 및 테스트](https://github.com/chujaeyeong/Shop-Project/commit/8f0e5f26c759e257b7e02c9bc1e88a7cf569f30a)
    - [주문 회원 조회 로직 수정](https://github.com/chujaeyeong/Shop-Project/commit/dc0fde9314dae823ba11912d468bd327e8394e49)

* #### `장바구니`
    - [장바구니 엔티티, DTO 생성](https://github.com/chujaeyeong/Shop-Project/commit/4fb20d08881cff8e76983c8cce894f941a4c2d1e)
    - [장바구니 담기 기능 구현 및 테스트](https://github.com/chujaeyeong/Shop-Project/commit/46554351d82586d3846c5a98b2b33d27de26e374)
    - [장바구니 엔티티 생성 및 테스트](https://github.com/chujaeyeong/Shop-Project/commit/8b9559e4eee7f9af441d365c3925449eb774efc9)
    - [장바구니 상품 목록 조회, 장바구니 상품 수량 수정 기능 구현](https://github.com/chujaeyeong/Shop-Project/commit/3b0c8537862cf43b6204df9668682a3a3a75cbc7)
    - [장바구니 상품 삭제 기능 구현](https://github.com/chujaeyeong/Shop-Project/commit/d6189564447fce7adb533ee6921f0e0b2c70b219)
    - [장바구니 상품 주문 기능 구현](https://github.com/chujaeyeong/Shop-Project/commit/083525e1070b525db92c6d3613ee16bcebe2b7d2)
    - [장바구니 회원 조회 로직 수정](https://github.com/chujaeyeong/Shop-Project/commit/56d4406013aaf5d29771a53e1e47e923b062da83)

* #### `마이페이지`
    - [마이페이지 기능 구현](https://github.com/chujaeyeong/Shop-Project/commit/9abd45c95efe1185962bd7ec5f81322b4f83e0fa)
    - [회원정보 수정 기능 추가 (일반회원)](https://github.com/chujaeyeong/Shop-Project/commit/8d5094a52f96bb207ea32e66a1b8216231cc57d9)
    - [회원정보 수정 시 입력값 검증, 비밀번호 보기 토글 추가](https://github.com/chujaeyeong/Shop-Project/commit/047e740dd4e86e2ee6b77be16118b88764d9619d)
    - [마이페이지 관련 기능 로직 분리, SNS 회원도 마이페이지 이용 가능하도록 로직 수정](https://github.com/chujaeyeong/Shop-Project/commit/6720681154db60bc98ed6cfc136610a45e481cd1)
    - [SNS 회원의 회원정보 수정 기능 구현](https://github.com/chujaeyeong/Shop-Project/commit/3902d402a90f50f51721886d9bc0cbc97c89e143)
    - [회원의 누적 구매 금액 표시 기능 추가](https://github.com/chujaeyeong/Shop-Project/commit/472e230121be92123ea48b6a9e0d2a5c2434a518)

* #### `상품 리뷰`
    - [구매한 상품의 리뷰 관리 기능 구현](https://github.com/chujaeyeong/Shop-Project/commit/36f5afe780ec3bb4de4f9eb35b01b2ec5a1d7eb0)
    - [리뷰 작성, 리뷰 수정, 리뷰 삭제 기능 구현](https://github.com/chujaeyeong/Shop-Project/commit/185bf73aa2c1a6723be58dda6f004ab6cb22df37)
    - [리뷰 CRUD시 reviewId 값을 null로 조회하는 에러 수정](https://github.com/chujaeyeong/Shop-Project/commit/ad547ff3bb55976eb86784a6778c5743dd399ddb)
    - [상품 상세페이지에 리뷰 리스트 보기 기능 구현](https://github.com/chujaeyeong/Shop-Project/commit/68224351c232291b92316f461d7f8d7744a0ee84)
    - [상품 상세페이지 하단의 리뷰 리스트 페이지 인증 해제](https://github.com/chujaeyeong/Shop-Project/commit/d9da2f56a624f89dd37200e06f72b5cc2e2fcb80)

* #### `기타`
    - [엔티티 공통 속성 공통화를 위한 리팩토링 (Auditing)](https://github.com/chujaeyeong/Shop-Project/commit/25317839cded2cc8193aeb02ad5b219d07910ec7)
    - [회원 엔티티 설계 수정](https://github.com/chujaeyeong/Shop-Project/commit/a28b924ebb362e5734e89a831e0c733c1c7753ed)
    - [헤더 네비게이션바 경로 수정](https://github.com/chujaeyeong/Shop-Project/commit/6d48a28d389ad07d8d65e2dbf650883ecb3781ea)

<br>
<br>

## 8. 개발 후기

<br>