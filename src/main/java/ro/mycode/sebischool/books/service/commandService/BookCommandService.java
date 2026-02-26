package ro.mycode.sebischool.books.service.commandService;

import ro.mycode.sebischool.books.service.dtos.BookResponse;
import ro.mycode.sebischool.books.service.dtos.Bookrequest;

public interface BookCommandService {
    BookResponse addBook(Long id, Bookrequest bookRequest);
    BookResponse updateBook(Long id, Bookrequest bookRequest);
    void deleteBook(Long id);
}
