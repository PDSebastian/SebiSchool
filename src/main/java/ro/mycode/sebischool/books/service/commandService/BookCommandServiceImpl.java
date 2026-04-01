package ro.mycode.sebischool.books.service.commandService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.books.exceptions.BookAlreadyExistsException;
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
    public BookResponse addBook(Long studentId, Bookrequest bookRequest) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException());

        if(bookRepository.existsByBookName(bookRequest.getBookName())){
            throw new BookAlreadyExistsException();
        }

        Book bookEntity = BookMapper.toEntity(bookRequest);
        bookEntity.setStudent(student);
        Book savedBook = bookRepository.save(bookEntity);
        return BookMapper.toDto(savedBook);
    }

    @Override
    @Transactional
    public BookResponse updateBook(Long id, Bookrequest bookRequest) {
        Book book=bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException());
        book.setBookName(bookRequest.getBookName());
        book.setCreatedAt(bookRequest.getCreatedAt());
        bookRepository.save(book);
        return BookMapper.toDto(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if(!bookRepository.existsById(id)) {throw new BookNotFoundException();}
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookResponse updatePatchBook(Long id, BookPatchRequest bookPatchRequest) {
        Book b=bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException());
        b.setBookName(bookPatchRequest.getBookName());
        b.setCreatedAt(bookPatchRequest.getCreatedAt());
        bookRepository.save(b);
        return BookMapper.toDto(bookRepository.save(b));
    }
}
