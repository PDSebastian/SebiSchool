package ro.mycode.sebischool.course.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class CourseFullException extends RuntimeException {
    public CourseFullException() {
        super(ErrorConstants.COURSE_ALREADY_FULL_ERROR);
    }
}
