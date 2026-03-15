package ro.mycode.sebischool.enrolment.mappers;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;


public class EnrolmentMapper {
    public static Enrolment toEntity(EnrolmentRequest enrolmentRequest) {
        if (enrolmentRequest == null) {
            return null;
        }
      return  Enrolment.builder().build();

    }
    public static EnrolmentResponse toDto(Enrolment enrolment) {
        if (enrolment == null) {
            return null;
        }
        return new  EnrolmentResponse(
                enrolment.getId(),
                enrolment.getCreatedAt()
        );

    }

}
