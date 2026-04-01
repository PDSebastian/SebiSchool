package ro.mycode.sebischool.course.dtos;

import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;

import java.util.List;

public record CourseDetailResponse(
        Long id,
        String name,
        String department,
        List<StudentSummaryResponse>students
) {
}
