package ro.mycode.sebischool.course.service.commandService;

import ro.mycode.sebischool.course.dtos.CoursePatchRequest;
import ro.mycode.sebischool.course.dtos.CourseRequest;
import ro.mycode.sebischool.course.dtos.CourseSummaryResponse;

public interface CourseCommandService {
    CourseSummaryResponse addCourse(CourseRequest courseRequest);
    CourseSummaryResponse deleteCourse(Long id);
    CourseSummaryResponse updateCourse(Long id, CourseRequest courseRequest);
    CourseSummaryResponse updatePatchCourse(Long id, CoursePatchRequest coursePatchRequest);
}
