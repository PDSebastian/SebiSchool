package ro.mycode.sebischool.books.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.mycode.sebischool.books.model.Book;

import java.util.List;


public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findAll();










}
