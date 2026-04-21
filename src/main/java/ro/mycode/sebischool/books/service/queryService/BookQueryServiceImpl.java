package ro.mycode.sebischool.books.service.queryService;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.books.exceptions.BookNotFoundException;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.repository.BookRepository;
import ro.mycode.sebischool.books.dtos.BookResponse;
import ro.mycode.sebischool.books.mapper.BookMapper;

import java.util.List;
import java.util.Optional;

@Component
public class BookQueryServiceImpl implements BookQueryService {
    BookRepository bookRepository;
   public BookQueryServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
   }

    @Override
    public List<BookResponse> findAllBooks() {
        return bookRepository.findAll().stream().map(BookMapper::toDto).toList();
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return  bookRepository.findAll().stream().map(BookMapper::toDto).toList();
    }

    @Override
    public BookResponse getBookByBookName(String bookName) {

       Optional<Book> book = bookRepository.findByBookName(bookName);
       if(book.isPresent()){
           return BookMapper.toDto(book.get());
       }
     throw   new BookNotFoundException();
    }




    @Override
    public BookResponse getBookById(Long bookId) {
       Optional<Book> book = bookRepository.findById(bookId);
       if(book.isPresent()){
           return BookMapper.toDto(book.get());
       }

        throw  new BookNotFoundException();
    }


}
