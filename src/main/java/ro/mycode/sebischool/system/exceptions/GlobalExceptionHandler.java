package ro.mycode.system.exceptions;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.mycode.sebischool.enrolment.exceptions.EnrolmentAlreadyExistsException;
import ro.mycode.sebischool.enrolment.exceptions.EnrolmentNotFoundException;
import ro.mycode.sebischool.student.exceptions.StudentAlreadyExistsException;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.system.constants.HintsConstants;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleStudentAlreadyExistsException(StudentAlreadyExistsException e) {
        ApiErrorResponse apiErrorResponse=ApiErrorResponse.builder()
                .hint(HintsConstants.STUDENT_ALREADY_EXISTS_HINT_MESSAGE)
                .message(e.getMessage())
                .dateTime(LocalDateTime.now())
                .state(HttpStatus.CONFLICT.value()).build();

        return  ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleStudentNotFoundException(StudentNotFoundException e) {
        ApiErrorResponse apiErrorResponse=ApiErrorResponse.builder()
                .hint(HintsConstants.STUDENT_NOT_FOUND_HINT_MESSAGE)
                .message(e.getMessage())
                .dateTime(LocalDateTime.now())
                .state(HttpStatus.NOT_FOUND.value()).build();

        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }
    @ExceptionHandler(EnrolmentAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEnrolmentAlreadyExistsException(EnrolmentAlreadyExistsException e) {
        ApiErrorResponse apiErrorResponse=ApiErrorResponse.builder()
                .hint(HintsConstants.ENROLMENT_ALREADY_EXISTS_HINT_MESSAGE)
                .message(e.getMessage())
                .dateTime(LocalDateTime.now())
                .state(HttpStatus.CONFLICT.value()).build();

        return  ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);
    }
    @ExceptionHandler(EnrolmentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEnrolmentNotFoundException(EnrolmentNotFoundException e) {
        ApiErrorResponse apiErrorResponse=ApiErrorResponse.builder()
                .hint(HintsConstants.ENROLMENT_NOT_FOUND_HINT_MESSAGE)
                .message(e.getMessage())
                .dateTime(LocalDateTime.now())
                .state(HttpStatus.NOT_FOUND.value()).build();

        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }





}
