package ro.mycode.sebischool.enrolment.service.commanService;

import ro.mycode.sebischool.enrolment.dtos.EnrolmentPatchRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;

public interface EnrolmentCommandService {
    EnrolmentResponse addEnrolment(EnrolmentRequest enrolmentRequest);
    EnrolmentResponse updateEnrolment(Long studentId ,EnrolmentRequest enrolmentRequest);
    EnrolmentResponse deleteEnrolment(Long id);
    EnrolmentResponse patchEnrolment(Long id , EnrolmentPatchRequest enrolmentRequest);
}
