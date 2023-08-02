# ♻️멋사마켓♻️
> 🥕당근마켓, 중고나라 등을 착안하여 중고 제품 거래 플랫폼을 만들어보는 미니 프로젝트입니다.

_사용자가 중고 물품을 자유롭게 올리고, 댓글을 통해 소통하며, 최종적으로 구매 제안에 대하여 수락할 수 있는 형태의 
중고 거래 플랫폼을 만드는 프로젝트입니다._

*****

## 📅 개발 기간
- (1차) 2023.06.29 ~ 2023.07.04
- (2차) 2023.07.26 ~ 2023.08.02

*****

## 📌 멋사마켓 ERD

![멋사마켓 ERD](ERD.png)

*****

## ⚙️ 개발 환경
### `JDK : 17.0.7`
### `IDE : IntelliJ IDEA`
### `Framework : Spring 6.0.10 (SpringBoot 3.1.1)`
### `DB : SQLite`
### `Dependency`
> Spring Web
> 
> Spring Boot DevTools
> 
> Spring Data JPA
> 
> Lombok
> 
> Validation
> 
> Spring Security
> 
> jjwt

*****

## 📃 기능 명세
### 사용 방법 (postman 기준)
- #### `토큰 발급 방법` : 회원가입으로 유저 생성 -> 생성된 유저로 로그인 -> JWT 발급
- #### `인증이 필요한 기능` : request 창의 Auth 탭 클릭 -> Type : Bearer -> Token : JWT 입력 후 request 진행
- #### `인증이 필요하지 않은 기능` : 바로 request 진행

### [postman_collection](https://documenter.getpostman.com/view/28054688/2s9XxtxFW1)
  > 자세한 기능 및 에러에 대한 결과는 위의 postman_collection 을 참조
  > 
  > 아래 End Point는 성공했을 때의 예시입니다.

- ### 📌 중고 물품 등록
  > 등록된 물품 단일 조회 및 전체 조회는 JWT 없이 누구나 조회 가능합니다.
  > 
  > 그 외의 기능은 인증된 사용자만 이용 가능합니다.

  <details>
  <summary>End Point</summary>
  <div>

  ### ➡️ 물품 등록
  
  ### `POST /items`

  Request Body:
  ```json
  {
      "title": "테스트1",
      "description": "테스트 설명",
      "minPriceWanted": 1000000
  }
  ```
  Response Status: 200
  
  Response Body:
  ```json
  {
      "message": "등록이 완료되었습니다."
  }
  ```

  ### ➡️ 등록된 물품 전체 조회
  
  ### `GET /items?page={page}&limit={limit}`
  
  Request Body: 없음
  
  Response Status: 200
  
  Response Body:
  
  ```json
  {
      "content": [
            {
                "id": 1,
                "title": "테스트 제목2",
                "username": "유저1",
                "description": "테스트 설명",
                "minPriceWanted": 1250000,
                "imageUrl": "/static/1/itemImage_1.jpg",
                "status": "판매 완료"
          },
            {
              "id": 2,
              "title": "테스트1",
              "username": "유저1",
              "description": "테스트 설명",
              "minPriceWanted": 1000000,
              "imageUrl": null,
              "status": "판매중"
          },
          // ...
      ],
      "totalPages": 4,
      "totalElements": 100,
      "last": false,
      "size": 25,
      "number": 1,
      "numberOfElements": 25,
      "first": false,
      "empty": false
  }
  ```
  
  ### ➡️ 등록된 물품 단일 조회
  
  ### `GET /items/{itemId}`
  
  Request Body: 없음
  
  Response Status: 200
  
  Response Body:
  
  ```json
  {
      "title": "테스트 제목2",
      "username": "유저1",
      "description": "테스트 설명",
      "minPriceWanted": 1250000,
      "status": "판매 완료"
  }
  ```
  
  ### ➡️ 물품 정보 수정
  
  ### `PUT /items/{itemId}`
  
  Request Body:
  
  ```json
  {
      "title": "테스트 제목",
      "description": "테스트 설명",
      "minPriceWanted": 1250000
  }
  ```
  Response Body:
  ```json
  {
      "message": "물품이 수정되었습니다."
  }
  ```
  
  ### ➡️ 이미지 등록
  
  ### `PUT /items/{itemId}/image`
  
  Request Body (Form Data):
  
  ```
  image:    image.(확장자) (file)
  ```
  
  Response Body:
  
  ```json
  {
      "message": "이미지가 등록되었습니다."
  }
  ```
  
  ### ➡️ 등록 물품 삭제
  
  ###  `DELETE /items/{itemId}`

  Response Body:
  ```json
  {
      "message": "물품을 삭제했습니다."
  }
  ```
  
  </div>
  </details>

- ### 📌 댓글 작성
  > 댓글 조회는 JWT 없이 누구나 조회 가능합니다.
  >
  > 그 외의 기능은 인증된 사용자만 이용 가능합니다.

  <details>
  <summary>End Point</summary>
  <div>
  
  ### ➡️ 댓글 등록
  
  ### `POST /items/{itemId}/comments`
  
  Request Body:
  
  ```json
  {
      "content": "할인 가능하신가요?"
  }
  ```
  
  Response Status: 200
  
  Response Body:
  
  ```json
  {
      "message": "댓글이 등록되었습니다."
  }
  ```
  
  ### ➡️ 댓글 조회
  
  ### `GET /items/{itemId}/comments`
  
  Request Body: 없음
  
  Response Status: 200
  
  Response Body:
  ```json
  {
      "content": [
          {
              "id": 1,
              "username": "유저2",
              "content": "할인 가능하신가요? 1000000 정도면 고려 가능합니다.",
              "reply": "됩니다."
          },
          {
              "id": 2,
              "username": "유저3",
              "content": "할인 가능하신가요?",
              "reply": null
          },
          // ...
      ],
      "totalPages": 4,
      "totalElements": 100,
      "last": false,
      "size": 25,
      "number": 1,
      "numberOfElements": 25,
      "first": false,
      "empty": false
  }
  ```
  
  ### ➡️ 댓글 수정
  
  ### `PUT /items/{itemId}/comments/{commentId}`
  
  Request Body:
  
    ```json
    {
        "content": "할인 가능하신가요? 1000000 정도면 고려 가능합니다."
    }
    ```
  
  Response Body:
  
    ```json
    {
        "message": "댓글이 수정되었습니다."
    }
    ```
  
  ### ➡️ 댓글에 답글 등록
  
  ### `PUT /items/{itemId}/comments/{commentId}/reply`
  
  Request Body:
  
  ```json
  {
      "reply": "됩니다."
  }
  ```
  
  Response Body:
  
  ```json
  {
      "message": "댓글에 답변이 추가되었습니다."
  }
  ```
  
  ### ➡️ 댓글 삭제
  
  ### `DELETE /items/{itemId}/comments/{commentId}`
  
  Response Body:
  ```json
  {
      "message": "댓글을 삭제했습니다."
  }
  ```
  
  </div>
  </details>

- ### 📌 구매 제안 및 거래
  > 구매 제안과 관련된 기능은 인증을 받은 사용자만 이용 가능합니다.

  <details>
  <summary>End Point</summary>
  <div>
  
  ### ➡️ 구매 제안 등록
  
  ### `POST /items/{itemId}/proposal`
  
  Request Body:
  
  ```json
  {
      "suggestedPrice": 1000000
  }
  ```
  
  Response Status: 200
  
  Response Body:
  
  ```json
  {
      "message": "구매 제안이 등록되었습니다."
  }
  ```
  
  ### ➡️ 등록된 구매 제안 조회
    - ### 물품 등록자 조회
      ### `GET /items/{itemId}/proposals?username=유저1&password=1234&page=1`
  
      Request Body: 없음
  
      Response Status: 200
  
      Response Body:
  
  ```json
  {
      "content": [
          {
              "id": 1,
              "username": "유저2",
              "suggestedPrice": 400000,
              "status": "제안"
          },
          {
              "id": 2,
              "username": "유저3",
              "suggestedPrice": 50000,
              "status": "거절"
          },
          // ...
      ],
      "totalPages": 4,
      "totalElements": 100,
      "last": false,
      "size": 25,
      "number": 1,
      "numberOfElements": 25,
      "first": false,
      "empty": false
  }
  ```
  
    - ### 제안 등록자 조회
      ### `GET /items/{itemId}/proposals?username=유저3&password=1234&page=1`
  
      Request Body: 없음
  
      Response Status: 200
  
      Response Body:
  
  ```json
  {
      "content": [
          {
              "id": 1,
              "username": "유저3",
              "suggestedPrice": 1200000,
              "status": "제안"
          }
          // ...
      ],
      "totalPages": 4,
      "totalElements": 100,
      "last": false,
      "size": 25,
      "number": 1,
      "numberOfElements": 25,
      "first": false,
      "empty": false
  }
  ```
  
  ### ➡️ 제안 수정 (가격 변경)
  
  ### `PUT /items/{itemId}/proposals/{proposalId}`
  
  Request Body:
  
  ```json
  {
      "suggestedPrice": 1200000
  }
  ```
  
  Response Body:
  
  ```json
  {
      "message": "제안이 수정되었습니다."
  }
  ```
  
  ### ➡️ 제안 수정 (제안 상태 변경)
  
  ### `PUT /items/{itemId}/proposals/{proposalId}`
  
  Request Body:
  
  ```json
  {
      "status": "수락" or "거절"
  }
  ```
  
  Response Body:
  
  ```json
  {
      "message": "제안의 상태가 변경되었습니다."
  }
  ```
  
  ### ➡️ 거래 확정
  
  ### `PUT /items/{itemId}/proposals/{proposalId}`
  
  Request Body:
  
  ```json
  {
      "status": "확정"
  }
  ```
  
  Response Body:
  
  ```json
  {
      "message": "구매가 확정되었습니다."
  }
  ```

  ### ➡️ 제안 삭제

  ### `DELETE /items/{itemId}/proposals/{proposalId}`

  Response Body:

  ```json
  {
      "message": "제안을 삭제했습니다."
  }
  ```
  
  </div>
  </details>

- ### 📌 회원가입 및 로그인
  > 회원가입 및 로그인의 경우 누구나 사용 가능합니다.

  <details>
  <summary>End Point</summary>
  <div>

  ### ➡️ 회원가입

  ### `POST /users/register`

  Request Body:

  ```json
  {
    "username" : "유저1",
    "password" : "1234",
    "passwordCheck" : "1234",
    "name" : "이름1",
    "phone" : "010-1234-1234",
    "email" : "유저1@gmail.com"
  }
  ```

  Response Body:

  ```json
  {
      "message": "회원가입이 완료되었습니다."
  }
  ```

  ### ➡️ 로그인 (토큰 발급)

  ### `POST /users/login`

  Request Body:

  ```json
  {
    "username" : "유저1",
    "password" : "1234"
  }
  ```

  Response Body:

  ```json
  {
      "token": "eyJhbGciOiJIUzUxMiJ9.
          eyJzdWIiOiLsnKDsoIAxIiwiaWF0IjoxNjkwNzMyODUwLCJleHAiOjE2OTA3MzY0NTB9.
          DNKXJZohjnNwC_dV8_w5uWZqwBHwTYZTKQrHRS3l4KmRoXOhFIp_GoPYFaYNEV8OCbGlb4
          QZusOlCIWG9tdVHg"
  }
  ```

  </div>
  </details>

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

### 📆 2023.07.26
#### 5️⃣ 회원가입 및 로그인 기능
- 회원가입 기능 구현
- 로그인 기능 구현
  - JWT 인증 로그인

### 📆 2023.07.27
#### 6️⃣ 각 Entity 연관관계 매핑
- ORM 기반 매핑
- 연관관계 매핑에 따른 Service 코드 리팩토링 

### 📆 2023.07.28
#### 7️⃣ 기능 접근 설정
- 인증된 사용자 접근 기능
  - username 및 password 가 포함되면 인증된 사용자만 사용할 수 있다.
- 인증되지 않은 사용자 접근 기능
  - 그 외의 URL은 누구든지 열람할 수 있다.

### 📆 2023.08.02
#### 8️⃣ UI 일부 구현
- 기능들에 대한 UI를 일부 구현했습니다.

*****