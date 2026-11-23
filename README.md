# GitHub Repository URL
> 
# Render Deployment URL
> 

# Source Code Exploration & Analysis
## Source Code Analysis
### ① Source Code Analysis
* Project Structure & JPA
  1. BookController, BookService, BookRepository의 역할과 요청 처리 흐름을 설명하세요.
    ```text
    BookController는 클라이언트를 통해서 들어온  HTTP 요청에 대해 입력값을 검증하고 BookService를 호출하고 HTTP 응답을 결과로 반환합니다. 
    BookService에서는 비즈니스 로직을 처리하고 BookRepository를 통해 DB에 접근합니다.
    BookRepository는 DB에 접근해서 CRUD 작업을 수행합니다.
    ```
  2. JpaRepository<Book, Long>만 선언해도 findAll(), save()를 사용할 수 있는 이유를 설명하세요.
    ```text
    BookRepository가 JpaRepository를 상속하기 때문입니다. 
    JpaRepository를 상속하면 메서드를 직접 구현하지 않아도 기본적인 CRUD 메서드를 사용할 수 있습니다.
    Book은 관리하는 Entity 타입이고 Long은 기본키 필드의 타입입니다.
    ```

* DTO & HTTP Response
  3. 기존 API가 Book Entity를 직접 요청·응답에 사용할 때 발생할 수 있는 문제를 설명하세요.
    ```text
    기존 API가 Book Entity를 직접 요청·응답에 사용할 때 서버 관리 필드 노출 및 변경 위험, Entity 변경 시 API 구조에 영향, 불필요한 데이터 필드까지 노출, 연관관계로 인한 순환 참조 문제가 발생할 수 있습니다.
    따라서 Entity와 DTO를 분리하여 사용하여야 합니다.
    ```
  4. Request DTO와 Response DTO를 분리하는 이유와 ResponseEntity가 필요한 상황을 설명하세요.
    ```text
    DTO란 계층 또는 클라이언트와 서버 사이에 데이터 전달을 위해 사용하는 객체입니다. 
    그 중 Request DTO는 클라이언트가 서버로 전달하는 데이터를 담당하고 Response DTO는 서버가 클라이언트로 반환하는 데이터를 담당합니다.
    요청과 응답 DTO를 분리하는 이유는 요청과 응답 구조에 차이가 있고 각각의 객체가 입력 전달과 출력 전달이라는 단일 목적만 가지고 있기 때문에 코드 가독성과 유지보수성이 높아지기 때문입니다. 
    ResponseEntity는 HTTP 응답 상태 코드, 헤더, 본문을 제어해야 하는 상황에서 필요합니다.  
    ```  

* Validation & Exception Handling

  5. @Valid, @NotBlank, @Min은 각각 어느 시점에 어떤 동작을 수행하나요?
    ```text
    위의 어노테이션들은 입력된 데이터가 정해진 조건을 만족하는지 확인하기 위해서 사용됩니다. 
    @Valid는 Controller에서 @RequestBody를 DTO로 변환한 후 메서드가 실행되기 전에 DTO의 검증 규칙을 검사합니다.
    @NotBlank와 @Min은 검증 조건을 지정하는 어노테이션입니다.
    @NotBlank는 null, 빈 문자열, 공백 문자열이 있는지 검사 및 존재한다면 불허하고 @Min은 입력된 숫자를 검사해서 지정된 최솟값과 같거나 큰 값이 입력되었는지 확인합니다. 
  
    ```
  6. @RestControllerAdvice와 @ExceptionHandler를 사용하면 예외 처리 흐름이 어떻게 바뀌나요?
    ```text
    @RestControllerAdvice는 여러 Controller의 예외를 공통 처리합니다.
    @ExceptionHandler는 지정한 예외를 처리하는 메서드입니다.
    이를 사용하면 예외가 발생했을 때 @RestControllerAdvice에서 예외 유형에 맞는 @ExceptionHandler를 찾아 연결하고  ResponseEntity가 HTTP 상태 코드와 오류 응답 반환합니다.
    ```

* Pagination & OpenAPI

  7. Pageable과 Page<T>는 각각 어떤 역할을 하며 sort=price,desc는 어떻게 처리되나요?
    ```text
    Pageable은 페이지 번호, 페이지 크기, 정렬 정보를 전달하는 인터페이스이고, Page<T>는 조회 데이터와 전체 페이지 등의 정보를 함께 제공합니다.
    sort=price,desc 요청은 가격을 기준으로 내림차순 정렬로 처리됩니다.
    ```
  8. /v3/api-docs와 /swagger-ui.html의 역할을 구분해서 설명하세요.
    ```text
    /v3/api-docs는 REST API를 기술하는 표준 명세를 담고 있습니다.
    /swagger-ui.html는 OpenAPI 명세를 웹 화면에 표시하고 테스트하는 도구입니다. 
    ```

### ② Line-by-Line Code Comments
* BookController.create() — @Valid, DTO, ResponseEntity, 201 응답
* BookService.findById() — Repository 조회, Optional, 404 예외 발생
  * BookController.list() 또는 BookService.findAll() — Pageable, Page, DTO 변환
  주요 실행 코드의 목적과 데이터 흐름을 설명합니다. import, 중괄호 등 단순 문법은 제외 가능합니다.
  기존 CRUD는 정상 동작해야 합니다.