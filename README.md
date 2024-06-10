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

#### `쇼핑몰 메인 (Main)`

| 기능           | URL   | Method | Request Parameters                                        | Description              |
|---------------|-------|--------|-----------------------------------------------------------|--------------------------|
| 메인 페이지 조회  | `/`   | GET    | `ItemSearchDto`, `Optional<Integer> page`, `Model model`  | 메인 페이지를 조회합니다.      |

<br>

#### `일반 회원 (Member)`
| 기능               | URL               | Method | Request Body / Parameters                                          | Description                  |
|--------------------|-------------------|--------|--------------------------------------------------------------------|------------------------------|
| 회원 가입 페이지 조회   | `/members/new`    | GET    | `Model model`                                                     | 회원 가입 페이지를 조회합니다.    |
| 회원 가입            | `/members/new`    | POST   | `MemberFormDto memberFormDto`, `BindingResult bindingResult`, `Model model` | 새로운 회원을 가입시킵니다.       |
| 로그인 페이지 조회     | `/members/login`  | GET    | -                                                                 | 로그인 페이지를 조회합니다.      |
| 로그인 에러 페이지 조회 | `/members/login/error` | GET    | `Model model`                                                     | 로그인 에러 페이지를 조회합니다.   |
| 회원 정보 수정 페이지 조회 | `/members/update` | GET    | `Model model`, `Authentication authentication`                    | 회원 정보 수정 페이지를 조회합니다. |
| 회원 정보 수정        | `/members/update` | POST   | `MemberFormDto memberFormDto`, `BindingResult bindingResult`, `Authentication authentication`, `RedirectAttributes redirectAttributes` | 회원 정보를 수정합니다.        |

<br>

#### `SNS 회원 (SocialMember, OAuth)`
| 기능                | URL                                | Method | Request Body / Parameters                | Description                             |
|---------------------|------------------------------------|--------|------------------------------------------|-----------------------------------------|
| 구글 로그인 성공     | `/login/oauth2/code/google`        | GET    | `OAuth2AuthenticationToken authentication` | 구글 로그인 성공 후 리다이렉트          |
| 네이버 로그인 성공    | `/login/oauth2/code/naver`         | GET    | `OAuth2AuthenticationToken authentication` | 네이버 로그인 성공 후 리다이렉트        |
| 로그인 실패          | `/loginFailure`                   | GET    | -                                        | 로그인 실패 후 리다이렉트                |

| 기능                | URL                          | Method | Request Body / Parameters                                          | Description                  |
|---------------------|------------------------------|--------|--------------------------------------------------------------------|------------------------------|
| 소셜 회원 정보 수정 폼  | `/social/member/updateForm`   | GET    | `Model model`                                                     | 소셜 회원 정보 수정 폼 페이지로 이동합니다.    |
| 소셜 회원 정보 수정    | `/social/member/update`       | POST   | `SocialMemberDto socialMemberDto`, `BindingResult result`, `Authentication authentication`, `Model model` | 소셜 회원 정보를 수정합니다.  |

<br>

## `마이페이지 (Profile)`
| 기능                | URL                  | Method | Request Body / Parameters                           | Description                       |
|---------------------|----------------------|--------|----------------------------------------------------|-----------------------------------|
| 마이페이지 조회         | `/member/mypage`     | GET    | `Model model`                                     | 마이페이지를 조회합니다.              |
| 프로필 수정 페이지 이동 | `/member/edit`       | GET    | `Authentication authentication`                   | 프로필 수정 페이지로 이동합니다.        |

<br>

#### `상품 (Item)`
| 기능               | URL                       | Method | Request Body / Parameters                                          | Description                   |
|--------------------|---------------------------|--------|--------------------------------------------------------------------|-------------------------------|
| 상품 등록 페이지 조회    | `/admin/item/new`          | GET    | `Model model`                                                     | 상품 등록 페이지를 조회합니다.   |
| 상품 등록            | `/admin/item/new`          | POST   | `ItemFormDto itemFormDto`, `BindingResult bindingResult`, `Model model`, `List<MultipartFile> itemImgFileList` | 새로운 상품을 등록합니다.        |
| 상품 상세 조회        | `/admin/item/{itemId}`      | GET    | `Long itemId`, `Model model`                                      | 상품 상세 정보를 조회합니다.    |
| 상품 수정            | `/admin/item/{itemId}`      | POST   | `ItemFormDto itemFormDto`, `BindingResult bindingResult`, `Model model`, `List<MultipartFile> itemImgFileList` | 상품 정보를 수정합니다.        |
| 상품 관리 페이지 조회    | `/admin/items`            | GET    | `ItemSearchDto itemSearchDto`, `Optional<Integer> page`, `Model model` | 상품 관리 페이지를 조회합니다.   |
| 상품 상세 페이지 조회    | `/item/{itemId}`           | GET    | `Long itemId`, `Model model`                                      | 상품 상세 페이지를 조회합니다.   |

<br>

#### `장바구니 (Cart)`
| 기능             | URL               | Method | Request Body / Parameters                               | Description                |
|------------------|-------------------|--------|---------------------------------------------------------|----------------------------|
| 장바구니에 상품 추가 | `/cart`           | POST   | `CartItemDto cartItemDto`, `BindingResult bindingResult`, `Principal principal` | 장바구니에 상품을 추가합니다.  |
| 장바구니 조회     | `/cart`           | GET    | `Principal principal`, `Model model`                    | 장바구니 목록을 조회합니다.    |
| 장바구니 아이템 수정| `/cartItem/{cartItemId}` | PATCH  | `Long cartItemId`, `int count`, `Principal principal`   | 장바구니 아이템 수량을 수정합니다. |
| 장바구니 아이템 삭제| `/cartItem/{cartItemId}` | DELETE | `Long cartItemId`, `Principal principal`                | 장바구니 아이템을 삭제합니다.   |
| 장바구니 아이템 주문| `/cart/orders`    | POST   | `CartOrderDto cartOrderDto`, `Principal principal`      | 장바구니 아이템을 주문합니다.  |

<br>

#### `주문 (Order)`
| 기능            | URL                      | Method | Request Body / Parameters                               | Description                |
|-----------------|--------------------------|--------|---------------------------------------------------------|----------------------------|
| 주문 생성       | `/order`                 | POST   | `OrderDto orderDto`, `BindingResult bindingResult`, `Principal principal` | 주문을 생성합니다.            |
| 주문 내역 조회   | `/orders`, `/orders/{page}` | GET    | `Optional<Integer> page`, `Principal principal`, `Model model` | 주문 내역을 조회합니다.       |
| 주문 취소       | `/order/{orderId}/cancel` | POST   | `Long orderId`, `Principal principal`                   | 주문을 취소합니다.           |

<br>

#### `상품 리뷰 (Review)`
| 기능               | URL                            | Method | Request Body / Parameters                                     | Description                |
|--------------------|--------------------------------|--------|--------------------------------------------------------------|----------------------------|
| 리뷰 생성 폼 조회       | `/reviews/new`                | GET    | `Long orderItemId`, `Model model`                            | 리뷰 생성 폼 페이지로 이동합니다.      |
| 리뷰 생성           | `/reviews/new`                | POST   | `ReviewFormDto reviewFormDto`, `BindingResult bindingResult`, `List<MultipartFile> reviewImages`, `Long orderItemId`, `UserDetails userDetails`, `CustomOAuth2User customOAuth2User`, `RedirectAttributes redirectAttributes`, `Model model` | 리뷰를 생성합니다.            |
| 리뷰 수정 폼 조회      | `/reviews/{reviewId}/edit`     | GET    | `Long reviewId`, `Model model`                               | 리뷰 수정 폼 페이지로 이동합니다.      |
| 리뷰 수정           | `/reviews/{reviewId}/edit`     | POST   | `Long reviewId`, `ReviewFormDto reviewFormDto`, `BindingResult bindingResult`, `List<MultipartFile> reviewImages`, `UserDetails userDetails`, `CustomOAuth2User customOAuth2User`, `RedirectAttributes redirectAttributes`, `Model model` | 리뷰를 수정합니다.            |
| 리뷰 삭제           | `/reviews/{reviewId}/delete`   | POST   | `Long reviewId`, `UserDetails userDetails`, `CustomOAuth2User customOAuth2User`, `RedirectAttributes redirectAttributes` | 리뷰를 삭제합니다.            |
| 리뷰 상세 조회       | `/reviews/details/{reviewId}`  | GET    | `Long reviewId`, `Model model`                               | 리뷰 상세 정보를 조회합니다.        |
| 리뷰 관리 페이지 조회  | `/reviews/manage`             | GET    | `UserDetails userDetails`, `CustomOAuth2User customOAuth2User`, `int page`, `Model model` | 리뷰 관리 페이지를 조회합니다.      |
| 특정 상품의 리뷰 조회  | `/reviews/item/{itemId}`      | GET    | `Long itemId`                                                | 특정 상품의 리뷰를 조회합니다.       |
| 리뷰 상세 정보 조회    | `/reviews/ajax/details/{reviewId}` | GET    | `Long reviewId`                                               | 리뷰의 상세 정보를 조회합니다.      |


<br>
<br>

## 6. 기능 구현

<br>
<br>

## 7. 기능별 작업 문서

<br>