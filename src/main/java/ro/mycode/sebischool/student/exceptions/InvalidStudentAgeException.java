package ro.mycode.sebischool.student.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class InvalidStudentAgeException extends RuntimeException {
    public InvalidStudentAgeException() {
        super(ErrorConstants.INVALID_STUDENT_AGE_ERROR);
    }
}
