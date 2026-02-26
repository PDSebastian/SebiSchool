package ro.mycode.sebischool.enrolment.exceptions;

public class EnrolmentAlreadyExistsException extends RuntimeException {
    public EnrolmentAlreadyExistsException(String message) {
        super(message);
    }
}
