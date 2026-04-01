package ro.mycode.sebischool.enrolment.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class EnrolmentNotFoundException extends RuntimeException {
    public EnrolmentNotFoundException() {
        super(ErrorConstants.ENROLMENT_NOT_FOUND_ERROR);
    }
}
