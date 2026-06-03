package ro.mycode.sebischool.auth.exceptions;

public class AuthValidationException extends RuntimeException {
    public AuthValidationException(String message) {
        super(message);
    }
}
