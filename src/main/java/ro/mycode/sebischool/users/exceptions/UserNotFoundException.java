package ro.mycode.sebischool.users.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super(ErrorConstants.USER_NOT_FOUND_ERROR);
    }
}
