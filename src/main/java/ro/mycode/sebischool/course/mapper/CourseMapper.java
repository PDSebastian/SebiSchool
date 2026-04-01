package ro.mycode.sebischool.course.mapper;

import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.dtos.CourseRequest;
import ro.mycode.sebischool.course.dtos.CourseSummaryResponse;
import ro.mycode.sebischool.student.mapper.StudentMapper;


public class CourseMapper {
    public static Course toEntity(CourseRequest courseRequest) {
        if(courseRequest == null) return null;
        return Course.builder().name(courseRequest.getName()).departament(courseRequest.getDepartament()).build();
    }
    public static CourseSummaryResponse toDto(Course course) {
        if(course == null) return null;
        return new CourseSummaryResponse(
                course.getId(),
                course.getName(),
                course.getDepartament(),
                course.getEnrolments().stream()
                        .map(enrolment -> StudentMapper.StudentToStudentSummaryResponse(enrolment.getStudent())).toList()
        );
    }
}
