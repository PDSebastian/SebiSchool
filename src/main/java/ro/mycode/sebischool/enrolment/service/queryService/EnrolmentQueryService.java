package ro.mycode.sebischool.enrolment.service.queryService;

import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;

import java.util.List;

public interface EnrolmentQueryService {
    List<EnrolmentResponse> getAllEnrolments();
    List<EnrolmentResponse> getAllEnrolmentsByCourseId(Long courseID);
    List<StudentSummaryResponse> getStudentsByCourseId(Long courseID);


}
