package edu.handong.week12.book;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class BookService {
  private final BookRepository repository;

  public BookService(BookRepository repository) {
    this.repository = repository;
  }

  // 도서 전체 조회
  // Pageable: 페이지 번호, 페이지 크기, 정렬 조건 포함
  public Page<BookResponse> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(BookResponse::from); // 각각의 Book을 BookResponse DTO로 변환
  }

  // 전달받은 ID로 도서 조회
  // Optional에 결과가 없다면 orElseThrow()가 BookNotFoundException 예외 발생
  public BookResponse findById(Long id) {
    return BookResponse.from(findEntity(id));
  } // TODO STEP 5: 404 --> findEntity() 통해 구현

  private Book findEntity(Long id) {
    return repository.findById(id).orElseThrow(() -> new BookNotFoundException(id)); // 조회 결과가 존재하지 않는다면 404 오류 발생 --> 공통 예외처리: 404 Not Found
  }

  public BookResponse create(BookRequest request) {
    Book book = new Book(request.title(), request.author(), request.price());
    return BookResponse.from(repository.save(book));
  }

  public BookResponse update(Long id, BookRequest request) {
    Book book = findEntity(id);
    book.update(request.title(), request.author(), request.price());
    return BookResponse.from(repository.save(book));
  }

  public void delete(Long id) {
    repository.delete(findEntity(id));
  }
}