package ro.mycode.sebischool.books.service.commandService;

import ro.mycode.sebischool.books.dtos.BookPatchRequest;
import ro.mycode.sebischool.books.dtos.BookResponse;
import ro.mycode.sebischool.books.dtos.Bookrequest;

public interface BookCommandService {
    BookResponse addBook(Long id, Bookrequest bookRequest);
    BookResponse updateBook(Long id, Bookrequest bookRequest);
    void deleteBook(Long id);
    BookResponse updatePatchBook(Long id, BookPatchRequest bookPatchRequest);
}
