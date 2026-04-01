package ro.mycode.sebischool.course.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseSummaryResponse {
   private long id;
   private String name;
   private String department;
    List<StudentSummaryResponse> students;

}
