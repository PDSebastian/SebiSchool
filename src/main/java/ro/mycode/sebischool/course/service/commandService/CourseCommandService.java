package ro.mycode.sebischool.course.service.commandService;

import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.service.dtos.CourseRequest;
import ro.mycode.sebischool.course.service.dtos.CourseResponse;

public interface CourseCommandService {
    CourseResponse addCourse(Long id, CourseRequest courseRequest);
    void deleteCourse(Long id);
    CourseResponse updateCourse(Long id, CourseRequest courseRequest);
}
