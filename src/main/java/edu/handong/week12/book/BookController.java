package edu.handong.week12.book;

import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "도서 CRUD API")
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "도서 목록 조회", description = "페이지와 정렬 조건을 적용해 도서 목록을 조회합니다.")
    public Page<BookResponse> list(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    // POST 요청
    @PostMapping
    // @Operation: Swagger UI에 도서 등록 API의 요약과 상세 설명을 표시
    @Operation(summary = "도서 등록", description = "새 도서를 등록합니다.")
    // @Valid: 제목, 저자, 가격이 DTO의 검증 조건을 만족하는지 검증
    // @RequestBody: 요청 JSON을 BookRequest DTO로 변환
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        BookResponse created = service.create(request);
        // 등록된 도서 정보를 BookResponse DTO에 담아 반환
        return ResponseEntity
                .created(URI.create("/api/books/" + created.id())) // ResponseEntity.created(): 응답 상태 201 Created
                .body(created);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
