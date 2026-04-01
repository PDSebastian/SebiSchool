package ro.mycode.sebischool.course.service.queryService;

import ro.mycode.sebischool.course.dtos.CourseSummaryResponse;

import java.util.List;

public interface CourseQueryService {
    List<CourseSummaryResponse> getAllCourses();
    CourseSummaryResponse getCourseById(Long id);
    CourseSummaryResponse getCourseByName(String name);
    List<CourseSummaryResponse> getAllCoursesWithEnrollment();

}
