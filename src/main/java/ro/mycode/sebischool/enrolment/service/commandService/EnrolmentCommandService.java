package ro.mycode.sebischool.enrolment.service.commandService;

import ro.mycode.sebischool.enrolment.dtos.EnrolmentPatchRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;

public interface EnrolmentCommandService {
    EnrolmentResponse addEnrolment(EnrolmentRequest enrolmentRequest);
    EnrolmentResponse updateEnrolment(Long studentId ,EnrolmentRequest enrolmentRequest);
    void deleteEnrolment(Long id);
    EnrolmentResponse patchEnrolment(Long id , EnrolmentPatchRequest enrolmentRequest);
}
