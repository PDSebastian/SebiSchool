package ro.mycode.sebischool.course.service.commandService;

import ro.mycode.sebischool.course.service.dtos.CoursePatchRequest;
import ro.mycode.sebischool.course.service.dtos.CourseRequest;
import ro.mycode.sebischool.course.service.dtos.CourseResponse;

public interface CourseCommandService {
    CourseResponse addCourse( CourseRequest courseRequest);
    CourseResponse deleteCourse(Long id);
    CourseResponse updateCourse(Long id, CourseRequest courseRequest);
    CourseResponse updatePatchCourse(Long id, CoursePatchRequest coursePatchRequest);
}
