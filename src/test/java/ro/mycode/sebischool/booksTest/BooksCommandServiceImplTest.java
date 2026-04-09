package ro.mycode.sebischool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.mycode.sebischool.books.dtos.BookPatchRequest;
import ro.mycode.sebischool.books.dtos.BookResponse;
import ro.mycode.sebischool.books.dtos.Bookrequest;
import ro.mycode.sebischool.books.exceptions.BookAlreadyExistsException;
import ro.mycode.sebischool.books.exceptions.BookNotFoundException;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.repository.BookRepository;
import ro.mycode.sebischool.books.service.commandService.BookCommandService;
import ro.mycode.sebischool.books.service.commandService.BookCommandServiceImpl;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class BooksCommandServiceImplTest {


    @Mock
    BookRepository bookRepository;
    BookCommandService bookCommandService;
    @Mock
    StudentRepository studentRepository;


    @BeforeEach
    void setUp() {
        bookCommandService = new BookCommandServiceImpl(bookRepository,studentRepository);
    }
    @Test
    void createThrowsWhenBookAlreadyExists() {
        Long studentId = 7L;
        String bookName = "Design Patterns";
        Bookrequest bookrequest = Bookrequest.builder()
                .bookName(bookName)
                .createdAt(LocalDateTime.now())
                .build();
        Student studentDB=Student.builder()
                .id(studentId)
                .firstName("Mihai")
                .lastName("Ionescu"
                ).age(21).email("test@mail.com").build();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentDB));
        when(bookRepository.existsByBookName(bookName)).thenReturn(Boolean.TRUE);
       assertThrows(BookAlreadyExistsException.class,()->{
           bookCommandService.addBook(7L,bookrequest);
       });

    }
        @Test
         void testAddBook(){
        Long studentId = 10L;
        String bookName = "ABCD";
        LocalDateTime dateTime= LocalDateTime.now();
        Bookrequest bookrequest=Bookrequest.builder().
                bookName(bookName).
                createdAt(dateTime)
                .build();

        Book book=Book.builder()
                .bookName(bookName)
                .createdAt(dateTime)
                .build();

        Book savedBook=Book.builder().
                bookName(bookName).
                createdAt(dateTime).build();
            Student student=Student.builder()
                    .id(studentId)
                    .firstName("Mihai")
                    .lastName("Ionescu"
                    ).age(21).email("test@mail.com").build();

        BookResponse expectedResponse=BookResponse.builder()
                .bookName(bookName).
                createdAt(dateTime).build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(bookRepository.save(book)).thenReturn(savedBook);
        BookResponse bookResponse=bookCommandService.addBook(studentId, bookrequest);
        assertEquals(bookResponse,expectedResponse);


        }
        @Test
        void testUpdateBookWhenBookNotFound(){
            Long studentId = 77L;
            String bookName = "ABCD";
            LocalDateTime dateTime= LocalDateTime.now();
            Bookrequest bookrequest=Bookrequest.builder().
                    bookName(bookName).
                    createdAt(dateTime)
                    .build();

            when(bookRepository.findById(studentId)).thenReturn(Optional.empty());
            assertThrows(BookNotFoundException.class,()->{
               bookCommandService.updateBook(studentId,bookrequest);
            });

        }
        @Test
    void testBookUpdate(){
            Long bookId = 2L;
            String newName = "Updated Name";
            LocalDateTime dateTime = LocalDateTime.now();

            Bookrequest bookrequest = Bookrequest.builder().bookName(newName).createdAt(dateTime).build();
            Book existingBook = Book.builder().id(bookId).bookName("Old Name").build();
            Book savedBook = Book.builder().id(bookId).bookName(newName).createdAt(dateTime).build();

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
            when(bookRepository.save(org.mockito.ArgumentMatchers.any(Book.class))).thenReturn(savedBook);

            BookResponse result = bookCommandService.updateBook(bookId, bookrequest);

            Assertions.assertNotNull(result);
            assertEquals(newName, result.getBookName());
            assertEquals(bookId, result.getId());

    }
    @Test
    void deleteBookWhenBookExists(){
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);
        bookCommandService.deleteBook(bookId);

    }
    @Test
    void deleteBookWhenBookNotFound(){
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);
        assertThrows(BookNotFoundException.class, () -> bookCommandService.deleteBook(bookId));
    }
    @Test
    void testUpdatePatchBook(){
        Long bookId = 2L;
        Book bookDb = Book.builder()
                .id(bookId)
                .bookName("Design Patterns")
                .build();

        BookPatchRequest patchRequest = BookPatchRequest.builder()
                .bookName("Patched Name")
                .createdAt(LocalDateTime.now())
                .build();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookDb));
        when(bookRepository.save(bookDb)).thenReturn(bookDb);
        BookResponse response = bookCommandService.updatePatchBook(bookId, patchRequest);
        assertEquals("Patched Name", response.getBookName());



    }




}
