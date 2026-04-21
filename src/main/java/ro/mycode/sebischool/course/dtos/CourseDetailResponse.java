package ro.mycode.sebischool.course.dtos;

import lombok.Builder;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;

import java.util.List;
@Builder
public record CourseDetailResponse(
        Long id,
        String name,
        String department,
        List<StudentSummaryResponse>students
) {
}
