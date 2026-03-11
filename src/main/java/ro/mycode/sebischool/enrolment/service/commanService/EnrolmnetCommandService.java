package ro.mycode.sebischool.enrolment.service.commanService;

import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentPatchRequest;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentResponse;

public interface EnrolmnetCommandService {
    EnrolmentResponse addEnrolment(EnrolmentRequest enrolmentRequest);
    EnrolmentResponse updateEnrolment(Long studentId ,EnrolmentRequest enrolmentRequest);
    EnrolmentResponse deleteEnrolment(Long id);
    EnrolmentResponse patchEnrolment(Long id , EnrolmentPatchRequest enrolmentRequest);
}
