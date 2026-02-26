package ro.mycode.sebischool.enrolment.exceptions;

public class EnrolmentNotFoundException extends RuntimeException {
    public EnrolmentNotFoundException(String message) {
        super(message);
    }
}
