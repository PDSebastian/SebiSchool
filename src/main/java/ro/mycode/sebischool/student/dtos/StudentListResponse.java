package ro.mycode.sebischool.student.dtos;

import java.util.List;

public record StudentListResponse(
        List<StudentSummaryResponse> students

) {
}
