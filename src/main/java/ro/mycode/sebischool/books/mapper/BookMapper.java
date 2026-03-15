package ro.mycode.sebischool.books.mapper;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.dtos.BookResponse;
import ro.mycode.sebischool.books.dtos.Bookrequest;


public class BookMapper {
    public static Book toEntity(Bookrequest bookrequest) {
        if(bookrequest == null) return null;
        return Book.builder().bookName(bookrequest.getBookName())
                .createdAt(bookrequest.getCreatedAt())
                .build();
    }
    public static  BookResponse toDto(Book book) {
        if(book == null) return null;
        return new BookResponse(
                book.getId(),
                book.getBookName(),
                book.getCreatedAt()
        );
    }










}
