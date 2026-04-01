package ro.mycode.sebischool.enrolment.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class EnrolmentAlreadyExistsException extends RuntimeException {
    public EnrolmentAlreadyExistsException() {

        super(ErrorConstants.ENROLMENT_ALREADY_EXISTS_ERROR);
    }
}
