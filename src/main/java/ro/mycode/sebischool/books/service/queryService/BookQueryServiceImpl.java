package ro.mycode.sebischool.books.service.queryService;

import jakarta.persistence.Column;
import org.springframework.stereotype.Component;
import ro.mycode.sebischool.books.exceptions.BookNotFoundException;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.repository.BookRepository;
import ro.mycode.sebischool.books.service.dtos.BookResponse;
import ro.mycode.sebischool.books.service.mapper.BookMapper;

import java.util.List;

@Component
public class BookQueryServiceImpl implements BookQueryService {
    BookMapper bookMapper;
    BookRepository bookRepository;
   public BookQueryServiceImpl(BookMapper bookMapper, BookRepository bookRepository) {
       this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
   }

    @Override
    public List<BookResponse> findAllBooks() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).toList();
    }





}
