# Project Information
## 프로젝트 소개
### Spring Boot REST API Advanced Customizing & Deployment
* 강의로부터 제공 받은 Book REST API Sample Project 실행 및 Spring Boot/JPA CRUD의 흐름 분석
* 12주차 강의 및 Lab 1~4의 내용 적용하여 REST API 고도화
* 최종 Render에 배포

## GitHub Repository URL
> https://github.com/2026-2-WebService/assign12-c01-22200252
## Render Deployment URL
> https://week12-sample-project-bdn7.onrender.com

---

# Installation
## 환경 설정
  * Java 17과 MariaDB를 사용합니다.
  * DB_URL, DB_USERNAME, DB_PASSWORD는 로컬 시스템 변수와 Render의 Environment Variables에 설정했습니다.

## 실행 방법
  * 로컬 실행: IntelliJ에서 Week12Application 실행 후 [http://localhost:8080/posts](http://localhost:8080/api/books) 로 접속합니다.
  * Render를 통해 배포된 페이지로 접속: 

---

# Source Code Exploration
## ① Source Code Analysis
* Project Structure & JPA
  1. BookController, BookService, BookRepository의 역할과 요청 처리 흐름을 설명하세요.
     * 관련 소스파일 / 메서드
       * BookController.java / create()
       * BookService.java
       * BookRepository.java / save()
    ```text
    BookController는 클라이언트를 통해서 들어온  HTTP 요청에 대해 입력값을 검증하고 BookService를 호출하고 HTTP 응답을 결과로 반환합니다. 
    BookService에서는 비즈니스 로직을 처리하고 BookRepository를 통해 DB에 접근합니다.
    BookRepository는 DB에 접근해서 CRUD 작업을 수행합니다.
    ```
  2. JpaRepository<Book, Long>만 선언해도 findAll(), save()를 사용할 수 있는 이유를 설명하세요.
     * 관련 소스파일 / 메서드
       * BookRepository.java / findAll(), save()
    ```text
    BookRepository가 JpaRepository를 상속하기 때문입니다. 
    JpaRepository를 상속하면 메서드를 직접 구현하지 않아도 기본적인 CRUD 메서드를 사용할 수 있습니다.
    Book은 관리하는 Entity 타입이고 Long은 기본키 필드의 타입입니다.
    ```

* DTO & HTTP Response
  3. 기존 API가 Book Entity를 직접 요청·응답에 사용할 때 발생할 수 있는 문제를 설명하세요.
     * 관련 소스파일 / 메서드
       * Book.java
       * BookController.java / create()
    ```text
    기존 API가 Book Entity를 직접 요청·응답에 사용할 때 서버 관리 필드 노출 및 변경 위험, Entity 변경 시 API 구조에 영향, 불필요한 데이터 필드까지 노출, 연관관계로 인한 순환 참조 문제가 발생할 수 있습니다.
    따라서 Entity와 DTO를 분리하여 사용하여야 합니다.
    ```
  4. Request DTO와 Response DTO를 분리하는 이유와 ResponseEntity가 필요한 상황을 설명하세요.
     * 관련 소스파일 / 메서드
       * BookRequest.java
       * BookResponse.java
       * BookController.java / create()
       * ResponseEntity.created()
    ```text
    DTO란 계층 또는 클라이언트와 서버 사이에 데이터 전달을 위해 사용하는 객체입니다. 
    그 중 Request DTO는 클라이언트가 서버로 전달하는 데이터를 담당하고 Response DTO는 서버가 클라이언트로 반환하는 데이터를 담당합니다.
    요청과 응답 DTO를 분리하는 이유는 요청과 응답 구조에 차이가 있고 각각의 객체가 입력 전달과 출력 전달이라는 단일 목적만 가지고 있기 때문에 코드 가독성과 유지보수성이 높아지기 때문입니다. 
    ResponseEntity는 HTTP 응답 상태 코드, 헤더, 본문을 제어해야 하는 상황에서 필요합니다.  
    ```  

* Validation & Exception Handling
  5. @Valid, @NotBlank, @Min은 각각 어느 시점에 어떤 동작을 수행하나요?
     * 관련 소스파일 / 메서드
       * BookController.java / create()
       * BookRequest.java
    ```text
    위의 어노테이션들은 입력된 데이터가 정해진 조건을 만족하는지 확인하기 위해서 사용됩니다. 
    @Valid는 Controller에서 @RequestBody를 DTO로 변환한 후 메서드가 실행되기 전에 DTO의 검증 규칙을 검사합니다.
    @NotBlank와 @Min은 검증 조건을 지정하는 어노테이션입니다.
    @NotBlank는 null, 빈 문자열, 공백 문자열이 있는지 검사 및 존재한다면 불허하고 @Min은 입력된 숫자를 검사해서 지정된 최솟값과 같거나 큰 값이 입력되었는지 확인합니다. 
  
    ```
  6. @RestControllerAdvice와 @ExceptionHandler를 사용하면 예외 처리 흐름이 어떻게 바뀌나요?
     * 관련 소스파일 / 메서드
       * BookService.java / findById()
    ```text
    @RestControllerAdvice는 여러 Controller의 예외를 공통 처리합니다.
    @ExceptionHandler는 지정한 예외를 처리하는 메서드입니다.
    이를 사용하면 예외가 발생했을 때 @RestControllerAdvice에서 예외 유형에 맞는 @ExceptionHandler를 찾아 연결하고  ResponseEntity가 HTTP 상태 코드와 오류 응답 반환합니다.
    ```

* Pagination & OpenAPI
  7. Pageable과 Page<T>는 각각 어떤 역할을 하며 sort=price,desc는 어떻게 처리되나요?
     * 관련 소스파일 / 메서드
       * BookController.java / list()
       * BookService.java / findAll()
       * BookRepository.java / findAll(Pageable)
    ```text
    Pageable은 페이지 번호, 페이지 크기, 정렬 정보를 전달하는 인터페이스이고, Page<T>는 조회 데이터와 전체 페이지 등의 정보를 함께 제공합니다.
    sort=price,desc 요청은 가격을 기준으로 내림차순 정렬로 처리됩니다.
    ```
  8. /v3/api-docs와 /swagger-ui.html의 역할을 구분해서 설명하세요.
     * 관련 소스파일 / 메서드: -
    ```text
    /v3/api-docs는 REST API를 기술하는 표준 명세를 담고 있습니다.
    /swagger-ui.html는 OpenAPI 명세를 웹 화면에 표시하고 테스트하는 도구입니다. 
    ```

## ② Line-by-Line Code Comments
* BookController.create() — @Valid, DTO, ResponseEntity, 201 응답
* BookService.findById() — Repository 조회, Optional, 404 예외 발생
* BookController.list() 또는 BookService.findAll() — Pageable, Page, DTO 변환

---

# Refactoring
## DTO와 ResponseEntity 변경 전
DTO 변경 전에는 요청 JSON이 들어오게 되면 Controller가 Book Entity로 받고 Service가 그 Entity를 저장하고 Book Entity가 응답으로 나갑니다.

가장 명확한 차이는 POST 요청으로 비교할 수 있습니다. 
Controller가 일반 Book을 반환하므로 응답 상태는 200 OK입니다.
![01_sample_post.png](results/01_sample_post.png)

## DTO와 ResponseEntity 변경 후 
DTO 변경 후에는 요청 JSON이 들어오게 되면 Controller가 BookRequest로 받고 Service가 그 Entity를 만들어서 저장하고 저장된 Book Entity가 BookResponse로 변환되어 응답으로 나갑니다.

Controller가 ResponseEntity.created(...)를 반환하므로 응답 상태는 201 Created입니다.
![02_dto_created.png](results/02_dto_created.png)

---

# New Features
## Validation
Controller의 POST와 PUT 요청에서 `@Valid`로 검증을 실행합니다. `BookRequest`에 `@NotBlank`를 적용하여 제목과 저자가 빈 값으로 등록되지 않게 했습니다. 또한 `@Min(0)`을 적용하여 가격이 음수값으로 등록되지 않게 했습니다.

* 잘못된 요청 검증 결과
![03_validation.png](results/03_validation.png)

## 공통 예외 처리
`@RestControllerAdvice`와 `@ExceptionHandler`를 사용하여 요청 처리 중 발생한 예외를 공통 처리합니다. 존재하지 않는 도서 ID에 대해 조회, 수정, 삭제를 시도했을 때 `404 Not Found` 오류 응답을 반환합니다.

* 잘못된 요청 공통 예외 처리 결과

  | 요청 | 결과 |
  |---|---|
  | `GET /api/books/999` | 404 Not Found |
  | `PUT /api/books/999` | 404 Not Found |
  | `DELETE /api/books/999` | 404 Not Found |
  ![04_not_found_get.png](results/04_not_found_get.png)
  ![04_not_found_put.png](results/04_not_found_put.png)
  ![04_not_found_delete.png](results/04_not_found_delete.png)

## Pagination/Sorting
* 요청 주소: GET /api/books?page=0&size=5&sort=price,desc 
* 응답 상태: 200 OK
* 응답 제이슨
  ```json
  {
      "content": [
          {
              "id": 5,
              "title": "테스트 도서 5",
              "author": "테스트 저자 5",
              "price": 32000
          },
          {
              "id": 9,
              "title": "테스트 도서 9",
              "author": "테스트 저자 9",
              "price": 28000
          },
          {
              "id": 2,
              "title": "테스트 도서 2",
              "author": "테스트 저자 2",
              "price": 25000
          },
          {
              "id": 7,
              "title": "테스트 도서 7",
              "author": "테스트 저자 7",
              "price": 22000
          },
          {
              "id": 4,
              "title": "테스트 도서 4",
              "author": "테스트 저자 4",
              "price": 18000
          }
      ],
      "pageable": {
          "pageNumber": 0,
          "pageSize": 5,
          "sort": {
              "empty": false,
              "sorted": true,
              "unsorted": false
          },
          "offset": 0,
          "paged": true,
          "unpaged": false
      },
      "last": false,
      "totalElements": 10,
      "totalPages": 2,
      "size": 5,
      "number": 0,
      "sort": {
          "empty": false,
          "sorted": true,
          "unsorted": false
      },
      "first": true,
      "numberOfElements": 5,
      "empty": false
  }
  ```
  ![05_pagination.png](results/05_pagination.png)

## Swagger
* Swagger UI: http://localhost:8080/swagger-ui.html
* OpenAPI JSON: http://localhost:8080/v3/api-docs
![06_swagger.png](results/06_swagger.png)

---

# Testing & Deployment
## results/ 스크린샷
1. 01_sample.png — 초기 Sample CRUD 테스트
![01_sample_post.png](results/01_sample_post.png)
![01_sample_get-all.png](results/01_sample_get-all.png)
![01_sample_get-by-id.png](results/01_sample_get-by-id.png)
![01_sample_put.png](results/01_sample_put.png)
![01_sample_delete.png](results/01_sample_delete.png)

2. 02_dto_created.png — DTO 적용 후 POST 성공 (201)
![02_dto_created.png](results/02_dto_created.png)
![02_dto_get-all.png](results/02_dto_get-all.png)
![02_dto_get-by-id.png](results/02_dto_get-by-id.png)
![02_dto_updated.png](results/02_dto_updated.png)
![02_dto_deleted.png](results/02_dto_deleted.png)

3. 03_validation.png — 잘못된 입력 요청 (400)
![03_validation.png](results/03_validation.png)

4. 04_not_found.png — 존재하지 않는 ID (404)
![04_not_found_get.png](results/04_not_found_get.png)
![04_not_found_put.png](results/04_not_found_put.png)
![04_not_found_delete.png](results/04_not_found_delete.png)

5. 05_pagination.png — 페이지/정렬 적용 결과 
![05_pagination.png](results/05_pagination.png)

6. 06_swagger.png — Swagger UI/API 테스트 화면
![06_swagger.png](results/06_swagger.png)

## 배포 URL
https://week12-sample-project-bdn7.onrender.com

---

# Weekly Review
## 새롭게 배운 내용 3가지
1. 기존 API가 Book Entity를 직접 요청·응답에 사용할 때 서버 관리 필드 노출 및 변경 위험, Entity 변경 시 API 구조에 영향, 불필요한 데이터 필드까지 노출, 연관관계로 인한 순환 참조 문제가 발생할 수 있다는 사실을 이번 과제를 통해 정리하면서 확실히 배울 수 있게 되었습니다. Entity와 DTO를 분리하여 사용하여야 하는 이유에 대해서 다시금 생각해볼 수 있었습니다.
2. 입력된 데이터가 정해진 조건을 만족하는지 확인하기 위해서 사용하는 어노테이션 중 @Min이 지정한 값보다 그저 큰 값이 아니라 같거나 큰 값을에 대해 검증한다는 사실을 이번 과제를 통해 배울 수 있었습니다.
3. Pageable을 활용하면 페이지 번호와 페이지 크기 그리고 정렬 조건을 요청으로 받아 DB에서 필요한 데이터만 조회하고 Page<T>로 조회 결과와 전체 페이지 정보를 함께 반환할 수 있다는 사실을 이번 과제를 통해 새롭게 배울 수 있었습니다.

## 어려웠던 점과 해결 방법
로컬에서 테스트를 실행하면서 제가 데이터베이스 내 어떤 테이블과 연결되어 있는지도 제대로 파악하지 못하고 테스트를 진행하고 있다는 사실을 깨달았습니다. ai의 도움을 받아 프로젝트 구조를 확인하고 연결된 데이터 베이스에 대해 확인하는 방법을 배우고 제대로 확인할 수 있었습니다.

## AI 활용 내용 및 느낀 점
강의자료 내 등장하는 코드 분석과 그를 기반으로 한 과제 코드 작성에 도움을 받았습니다. 
관련 개념을 분석하코 기존의 코드를 이해하는 데에 도움을 받았습니다.