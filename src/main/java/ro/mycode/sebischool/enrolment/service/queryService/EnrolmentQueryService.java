package ro.mycode.sebischool.enrolment.service.queryService;

import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentResponse;

import java.util.List;

public interface EnrolmentQueryService {
    List<EnrolmentResponse> getAllEnrolments();


}
