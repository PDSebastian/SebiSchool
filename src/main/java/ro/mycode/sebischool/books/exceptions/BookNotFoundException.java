package ro.mycode.sebischool.books.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
        super(ErrorConstants.BOOK_NOT_FOUND_ERROR);
    }
}
