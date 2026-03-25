package ro.mycode.sebischool.course.service.mapper;

import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.dtos.CourseRequest;
import ro.mycode.sebischool.course.dtos.CourseResponse;


public class CourseMapper {
    public static Course toEntity(CourseRequest courseRequest) {
        if(courseRequest == null) return null;
        return Course.builder().name(courseRequest.getName()).departament(courseRequest.getDepartament()).build();
    }
    public static CourseResponse toDto(Course course) {
        if(course == null) return null;
        return new CourseResponse(
                course.getId(),
                course.getName(),
                course.getDepartament()
        );
    }
}
