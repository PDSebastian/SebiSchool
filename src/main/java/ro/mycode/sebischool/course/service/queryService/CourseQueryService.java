package ro.mycode.sebischool.course.service.queryService;

import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.service.dtos.CourseResponse;

import java.util.List;

public interface CourseQueryService {
    List<CourseResponse> getAllCourses();
    CourseResponse getCourseById(int id);
    CourseResponse getCourseByName(String name);
    List<CourseResponse> getAllCoursesWithEnrollment();

}
