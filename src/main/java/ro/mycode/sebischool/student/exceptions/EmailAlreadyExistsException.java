package ro.mycode.sebischool.student.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super(ErrorConstants.EMAIL_ALREADY_EXISTS_ERROR);
    }
}
