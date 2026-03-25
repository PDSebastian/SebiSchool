package ro.mycode.sebischool.system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.mycode.sebischool.books.exceptions.BookAlreadyExistsException;
import ro.mycode.sebischool.books.exceptions.BookNotFoundException;
import ro.mycode.sebischool.course.exceptions.CourseAlreadyExistsException;
import ro.mycode.sebischool.course.exceptions.CourseFullException;
import ro.mycode.sebischool.course.exceptions.CourseNotFoundException;
import ro.mycode.sebischool.enrolment.exceptions.EnrolmentAlreadyExistsException;
import ro.mycode.sebischool.enrolment.exceptions.EnrolmentNotFoundException;
import ro.mycode.sebischool.student.exceptions.InvalidStudentAgeException;
import ro.mycode.sebischool.student.exceptions.StudentAlreadyExistsException;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptions {
    @ExceptionHandler({
            StudentNotFoundException.class,
            BookNotFoundException.class,
            EnrolmentNotFoundException.class,
            CourseNotFoundException.class

    })
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException e) {
            ApiErrorResponse apiErrorResponse=ApiErrorResponse.builder()
                    .message(e.getMessage())
                    .state(HttpStatus.BAD_REQUEST.value())
                    .dateTime(LocalDateTime.now())
                    .build();

            return  ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);

    }
    @ExceptionHandler({
            StudentAlreadyExistsException.class,
            CourseAlreadyExistsException.class,
            EnrolmentAlreadyExistsException.class,
            BookAlreadyExistsException.class


    })
    public ResponseEntity<ApiErrorResponse> handleConflict(RuntimeException e) {
        ApiErrorResponse apiErrorResponse=ApiErrorResponse.builder()
                .message(e.getMessage())
                .state(HttpStatus.BAD_REQUEST.value())
                .dateTime(LocalDateTime.now())
                .build();
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);
    }
    @ExceptionHandler({
            InvalidStudentAgeException.class,
            CourseFullException.class


    })
    public ResponseEntity<ApiErrorResponse> handleInvalidStudentAge(RuntimeException e) {
        ApiErrorResponse apiErrorResponse=ApiErrorResponse.builder()
                .message(e.getMessage())
                .state(HttpStatus.BAD_REQUEST.value())
                .dateTime(LocalDateTime.now())
                .build();
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

}
