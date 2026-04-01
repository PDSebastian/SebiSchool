package ro.mycode.sebischool.books.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.mycode.sebischool.books.model.Book;

import java.util.Optional;


public interface BookRepository extends JpaRepository<Book,Long> {


    Optional<Book> findByBookName(String bookName);

    boolean existsByBookName(String bookName);
}
