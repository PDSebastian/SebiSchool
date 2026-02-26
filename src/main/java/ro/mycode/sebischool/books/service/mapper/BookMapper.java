package ro.mycode.sebischool.books.service.mapper;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.service.dtos.BookResponse;
import ro.mycode.sebischool.books.service.dtos.Bookrequest;

@Component
public class BookMapper {
    public Book toEntity(Bookrequest bookrequest) {
        if(bookrequest == null) return null;
        return Book.builder().bookName(bookrequest.getBookName())
                .createdAt(bookrequest.getCreatedAt())
                .build();
    }
    public BookResponse toDto(Book book) {
        if(book == null) return null;
        return new BookResponse(
                book.getId(),
                book.getBookName(),
                book.getCreatedAt()
        );
    }










}
