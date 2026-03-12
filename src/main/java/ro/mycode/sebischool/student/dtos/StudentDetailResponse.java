package ro.mycode.sebischool.student.dtos;

import ro.mycode.sebischool.books.service.dtos.BookResponse;
import ro.mycode.sebischool.course.service.dtos.CourseResponse;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentResponse;

import java.util.List;

public record StudentDetailResponse(String firstName, String lastName, String email,
                                    List<EnrolmentResponse> enrolmentResponses, List<CourseResponse>courseResponses) {
}
