package ro.mycode.sebischool.books.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException() {
        super(ErrorConstants.BOOK_ALREADY_EXISTS_ERROR);
    }
}
