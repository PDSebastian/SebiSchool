package ro.mycode.sebischool.books.service.commandService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.books.exceptions.BookNotFoundException;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.repository.BookRepository;
import ro.mycode.sebischool.books.dtos.BookPatchRequest;
import ro.mycode.sebischool.books.dtos.BookResponse;
import ro.mycode.sebischool.books.dtos.Bookrequest;
import ro.mycode.sebischool.books.mapper.BookMapper;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;

@Component
public class BookCommandServiceImpl implements BookCommandService {
    private final StudentRepository studentRepository;
    BookRepository bookRepository;

    BookCommandServiceImpl(BookRepository bookRepository,  StudentRepository studentRepository) {
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }
    @Override
    @Transactional
    public BookResponse addBook(Long id, Bookrequest bookRequest) {
        Book b=BookMapper.toEntity(bookRequest);
        Student s=studentRepository.findById(id).orElseThrow(()->new StudentNotFoundException("Student Not Found"));
        b.setStudent(s);
        Book book=bookRepository.save(b);
        return BookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookResponse updateBook(Long id, Bookrequest bookRequest) {
        Book book=bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
        book.setBookName(bookRequest.getBookName());
        book.setCreatedAt(bookRequest.getCreatedAt());
        bookRepository.save(book);
        return BookMapper.toDto(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if(!bookRepository.existsById(id)) {throw new BookNotFoundException("Book not found");}
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookResponse updatePatchBook(Long id, BookPatchRequest bookPatchRequest) {
        Book b=bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
        b.setBookName(bookPatchRequest.getBookName());
        b.setCreatedAt(bookPatchRequest.getCreatedAt());
        bookRepository.save(b);
        return BookMapper.toDto(bookRepository.save(b));
    }
}
