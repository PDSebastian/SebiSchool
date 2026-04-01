package ro.mycode.sebischool.course.exceptions;

import ro.mycode.sebischool.system.constants.ErrorConstants;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException() {
        super(ErrorConstants.COURSE_NOT_FOUND_ERROR);
    }
}
