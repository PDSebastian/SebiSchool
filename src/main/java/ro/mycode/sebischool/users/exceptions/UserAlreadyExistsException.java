package ro.mycode.sebischool.users.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super(ErrorConstants.USER_ALREADY_EXISTS_ERROR);
    }
}
