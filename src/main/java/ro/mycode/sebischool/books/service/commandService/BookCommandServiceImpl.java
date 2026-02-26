package ro.mycode.sebischool.books.service.commandService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.books.exceptions.BookAlreadyExistsException;
import ro.mycode.sebischool.books.exceptions.BookNotFoundException;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.repository.BookRepository;
import ro.mycode.sebischool.books.service.dtos.BookResponse;
import ro.mycode.sebischool.books.service.dtos.Bookrequest;
import ro.mycode.sebischool.books.service.mapper.BookMapper;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;

@Component
public class BookCommandServiceImpl implements BookCommandService {
    private final StudentRepository studentRepository;
    BookRepository bookRepository;
    BookMapper bookMapper;
    BookCommandServiceImpl(BookRepository bookRepository, BookMapper bookMapper, StudentRepository studentRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.studentRepository = studentRepository;
    }
    @Override
    @Transactional
    public BookResponse addBook(Long id, Bookrequest bookRequest) {
        Book b=bookMapper.toEntity(bookRequest);
        Student s=studentRepository.findById(id).orElseThrow(()->new StudentNotFoundException("Student Not Found"));
        b.setStudent(s);
        Book book=bookRepository.save(b);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookResponse updateBook(Long id, Bookrequest bookRequest) {
        Book book=bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
        book.setBookName(bookRequest.getBookName());
        book.setCreatedAt(bookRequest.getCreatedAt());
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if(!bookRepository.existsById(id)) {throw new BookNotFoundException("Book not found");}
        bookRepository.deleteById(id);
    }
}
