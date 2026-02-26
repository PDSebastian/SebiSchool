package ro.mycode.sebischool.enrolment.service.commanService;

import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentResponse;

public interface EnrolmnetCommandService {
    EnrolmentResponse addEnrolment(Long studentId ,EnrolmentRequest enrolmentRequest);
    EnrolmentResponse updateEnrolment(Long studentId ,EnrolmentRequest enrolmentRequest);
    void deleteEnrolment(Long id);
}
