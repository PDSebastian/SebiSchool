package ro.mycode.sebischool.student.dtos;

import ro.mycode.sebischool.books.dtos.BookResponse;
import ro.mycode.sebischool.course.dtos.CourseResponse;

import java.util.List;

public record StudentDetailResponse(Long Id,String firstName, String lastName, String email,List<CourseResponse>courses,
                                    List<BookResponse> books) {
}
