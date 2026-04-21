package ro.mycode.sebischool.booksTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.mycode.sebischool.books.dtos.BookResponse;
import ro.mycode.sebischool.books.exceptions.BookNotFoundException;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.repository.BookRepository;
import ro.mycode.sebischool.books.service.queryService.BookQueryService;
import ro.mycode.sebischool.books.service.queryService.BookQueryServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class BooksQueryServiceImplTest {
    @Mock
    BookRepository bookRepository;
    BookQueryService bookQueryService;

    @BeforeEach
    void setUp() {
        bookQueryService = new BookQueryServiceImpl(bookRepository);
    }
    @Test
    void testfindAllBooks(){
        Long id1=3L;
        Long id2=4L;
        Book book = Book.builder().id(id1).bookName("Introduction to Algorithms").createdAt(LocalDateTime.now()).build();
        Book book1= Book.builder().id(id2).bookName("The Pragmatic Programmer").createdAt(LocalDateTime.now()).build();

        when(bookRepository.findAll()).thenReturn(List.of(book,book1));
        List<BookResponse> list = bookQueryService.findAllBooks();

        assertEquals(2,list.size());


    }
    @Test
    void testGetBookByName(){
        String bookName = "Introduction to Algorithms";
        Book b=Book.builder().bookName(bookName).createdAt(LocalDateTime.now()).build();
        when(bookRepository.findByBookName(b.getBookName())).thenReturn(Optional.of(b));
        BookResponse book=bookQueryService.getBookByBookName(bookName);
        assertEquals(bookName,book.getBookName());


    }
    @Test
  void testGetBookById(){
        Long id=3L;
        Book book=Book.builder().id(id).createdAt(LocalDateTime.now()).build();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        BookResponse bookResponse=bookQueryService.getBookById(id);
        assertEquals(book.getBookName(),bookResponse.getBookName());
    }
    @Test
    void testGetBookByBookNameWhenBookNotFound(){
        String bookName = "dwdwd";
        when(bookRepository.findByBookName(bookName)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class,()->bookQueryService.getBookByBookName(bookName));
    }
    @Test
    void testGetAllBooks(){
        Long id1=3L;
        Long id2=4L;
        Book b1=Book.builder().id(id1).bookName("Introduction to Algorithms").createdAt(LocalDateTime.now()).build();
        Book b2=Book.builder().id(id2).bookName("The Pragmatic Programmer").createdAt(LocalDateTime.now()).build();
        when(bookRepository.findAll()).thenReturn(List.of(b1,b2));
        List<BookResponse> list = bookQueryService.getAllBooks();
        assertEquals(2,list.size());
    }
    @Test
   void testGetBookByIdWhenBookNotFound(){
        Long id=3L;
        Book book=Book.builder().id(id).createdAt(LocalDateTime.now()).build();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class,()->bookQueryService.getBookById(id));

    }
}
