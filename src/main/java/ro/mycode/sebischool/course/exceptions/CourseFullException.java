package ro.mycode.sebischool.course.exceptions;

public class CourseFullException extends RuntimeException {
  public CourseFullException(String message) {
    super(message);
  }
}
