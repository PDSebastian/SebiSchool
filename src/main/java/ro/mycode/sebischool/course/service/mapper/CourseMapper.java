package ro.mycode.sebischool.course.service.mapper;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.service.dtos.CourseRequest;
import ro.mycode.sebischool.course.service.dtos.CourseResponse;

import javax.swing.text.html.parser.Entity;

@Component
public class CourseMapper {
    public Course toEntity(CourseRequest courseRequest) {
        if(courseRequest == null) return null;
        return Course.builder().name(courseRequest.getName()).departament(courseRequest.getDepartament()).build();
    }
    public CourseResponse toDto(Course course) {
        if(course == null) return null;
        return new CourseResponse(
                course.getId(),
                course.getName(),
                course.getDepartament()
        );
    }
}
