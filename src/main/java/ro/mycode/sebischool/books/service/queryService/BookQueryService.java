package ro.mycode.sebischool.books.service.queryService;

import ro.mycode.sebischool.books.dtos.BookResponse;

import java.util.List;
import java.util.Optional;

public interface BookQueryService {
    List<BookResponse>findAllBooks();

    List<BookResponse> getAllBooks();

     BookResponse getBookByBookName(String bookName);
     BookResponse getBookById(Long bookId);
}
