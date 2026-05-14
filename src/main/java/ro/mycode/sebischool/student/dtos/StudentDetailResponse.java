package ro.mycode.sebischool.student.dtos;

import ro.mycode.sebischool.books.dtos.BookResponse;
import ro.mycode.sebischool.course.dtos.CourseSummaryResponse;

import java.util.List;


public record StudentDetailResponse(Long Id, String firstName, String lastName, String email, List<CourseSummaryResponse>courses,
                                    List<BookResponse> books) {
}
