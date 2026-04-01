package ro.mycode.sebischool.student.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException() {

        super(ErrorConstants.STUDENT_NOT_FOUND_ERROR);
    }
}
