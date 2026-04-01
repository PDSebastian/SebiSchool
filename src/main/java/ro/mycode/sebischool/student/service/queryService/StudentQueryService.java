package ro.mycode.sebischool.student.service.queryService;

import ro.mycode.sebischool.student.dtos.StudentDetailResponse;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;

import java.util.List;
import java.util.Optional;

public interface StudentQueryService {
    List<StudentSummaryResponse> getAllStudents();
    Optional<StudentSummaryResponse> getStudentByEmail(String email);
    Optional<StudentSummaryResponse> getStudentByFirstNameAndLastName(String firstName, String lastName);

    StudentDetailResponse getStudentById(Long studentID);

    List<StudentSummaryResponse> getStudentsByFirstName(String firstName);
    StudentSummaryResponse getStudentWithEnrolments(Long studentID);
}
