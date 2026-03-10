package ro.mycode.sebischool.books.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.service.dtos.BookResponse;

import java.util.List;
import java.util.Optional;


public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findAll();


    Optional<BookResponse> findByBookName(String bookName);
}
