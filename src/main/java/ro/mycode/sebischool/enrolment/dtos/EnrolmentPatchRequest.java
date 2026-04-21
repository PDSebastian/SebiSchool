package ro.mycode.sebischool.enrolment.dtos;

import lombok.Builder;

@Builder
public record EnrolmentPatchRequest(
      Long courseId,
      Long studentId


) {
}
