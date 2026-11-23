package edu.handong.week12.book;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
  private final BookRepository repository;

  public BookService(BookRepository repository) {
    this.repository = repository;
  }

  public List<BookResponse> findAll() {
    return repository.findAll().stream()
            .map(BookResponse::from)
            .collect(Collectors.toList());
  } // TODO STEP 6: Pagination/Sorting

  public BookResponse findById(Long id) {
    return BookResponse.from(findEntity(id));
  } // TODO STEP 5: 404

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
    repository.deleteById(id);
  } // TODO: 404 if absent

  private Book findEntity(Long id) {
    return repository.findById(id).orElseThrow();
  }
}
