# ♻️멋사마켓♻️
> 🥕당근마켓, 중고나라 등을 착안하여 중고 제품 거래 플랫폼을 만들어보는 미니 프로젝트입니다.

_사용자가 중고 물품을 자유롭게 올리고, 댓글을 통해 소통하며, 최종적으로 구매 제안에 대하여 수락할 수 있는 형태의 
중고 거래 플랫폼을 만드는 프로젝트입니다._

*****

## 📅 개발 기간
- 2023.06.29 ~ 2023.07.04
*****

## ⚙️ 개발 환경
### JDK
> 17.0.7
### IDE
> IntelliJ IDEA
### Dependency
> Spring Web
> 
> Spring Boot DevTools
> 
> Spring Data JPA
> 
> Lombok
> 
> Validation
### DB
> SQLite

*****

## 📢 주요 기능
- ### 중고 물품 등록
- ### 댓글 작성
- ### 구매 제안 및 거래 기능

*****

## End Point
### 중고 물품 등록
> POST /items

> GET /items?page={page}&limit={limit}

> GET /items/{itemId}

> PUT /items/{itemId}

> PUT /items/{itemId}/image

> DELETE /items/{itemId}

### 댓글 작성
> POST /items/{itemId}/comments

> GET /items/{itemId}/comments

> PUT /items/{itemId}/comments/{commentId}

> PUT /items/{itemId}/comments/{commentId}/reply

> DELETE /items/{itemId}/comments/{commentId}

### 구매 제안 및 거래
> POST /items/{itemId}/proposals

> GET /items/{itemId}/proposals?writer=(writer)&password=(password)&page=(page)
 
> PUT /items/{itemId}/proposals/{proposalId}

> DELETE /items/{itemId}/proposals/{proposalId}

*****

## 📃 개발 내역
### 📆 2023.06.29
#### 1️⃣ 프로젝트 생성
- 프로젝트 생성 및 환경설정

#### 2️⃣ 중고 물품 관리 기능
- 물품 CRUD 구현
- 물품 전체 조회 페이징
- 물품 이미지 업로드 기능

### 📆 2023.07.03
#### 3️⃣ 물품 게시글 내 댓글 기능
- 댓글 CRUD 구현
- 댓글 전체 조회 페이징
- 댓글에 대한 물품 등록자 답글 기능

### 📆 2023.07.04
#### 4️⃣ 구매 제안 및 거래 기능
- 구매 제안 CRUD 구현
- 구매 제안 조회 페이징
  - 물품 등록자인 경우, 등록된 물품에 대한 구매 제안 전체 조회 가능
  - 구매 제안자인 경우, 자신이 제안한 구매 제안 전체 조회 가능
- 물품 구매 제안의 수락/거절에 따른 구매 확정 기능

*****