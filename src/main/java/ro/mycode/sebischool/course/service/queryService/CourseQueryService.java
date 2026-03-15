package ro.mycode.sebischool.course.service.queryService;

import ro.mycode.sebischool.course.dtos.CourseResponse;

import java.util.List;

public interface CourseQueryService {
    List<CourseResponse> getAllCourses();
    CourseResponse getCourseById(Long id);
    CourseResponse getCourseByName(String name);
    List<CourseResponse> getAllCoursesWithEnrollment();

}
