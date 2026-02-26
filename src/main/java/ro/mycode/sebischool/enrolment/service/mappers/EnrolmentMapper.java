package ro.mycode.sebischool.enrolment.service.mappers;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentResponse;

@Component
public class EnrolmentMapper {
    public Enrolment toEntity(EnrolmentRequest enrolmentRequest) {
        if (enrolmentRequest == null) {
            return null;
        }
      return  Enrolment.builder().createdAt(enrolmentRequest.getCreatedAt()).build();

    }
    public EnrolmentResponse toDto(Enrolment enrolment) {
        if (enrolment == null) {
            return null;
        }
        return new  EnrolmentResponse(
                enrolment.getId(),
                enrolment.getCreatedAt()
        );

    }

}
