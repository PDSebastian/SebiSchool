package ro.mycode.sebischool.course.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class CourseAlreadyExistsException extends RuntimeException {
    public CourseAlreadyExistsException() {
        super(ErrorConstants.COURSE_ALREADY_EXISTS_ERROR);
    }
}
