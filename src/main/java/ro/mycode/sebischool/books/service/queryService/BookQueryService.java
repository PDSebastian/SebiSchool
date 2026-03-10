package ro.mycode.sebischool.books.service.queryService;

import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.service.dtos.BookResponse;

import java.util.List;

public interface BookQueryService {
    List<BookResponse>findAllBooks();

    List<BookResponse> getAllBooks();

    BookResponse getBookByBookName(String bookName);
}
