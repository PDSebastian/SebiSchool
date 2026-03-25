package ro.mycode.sebischool.student.exceptions;

public class InvalidStudentAgeException extends RuntimeException {
  public InvalidStudentAgeException(String message) {
    super(message);
  }
}
