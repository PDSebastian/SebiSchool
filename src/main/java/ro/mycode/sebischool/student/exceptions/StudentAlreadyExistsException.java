package ro.mycode.sebischool.student.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class StudentAlreadyExistsException extends RuntimeException {
    public StudentAlreadyExistsException() {

        super(ErrorConstants.STUDENT_ALREADY_EXISTS_ERROR);
    }
}
