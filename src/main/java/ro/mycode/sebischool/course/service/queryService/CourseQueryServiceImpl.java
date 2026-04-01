package ro.mycode.sebischool.course.service.queryService;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.course.exceptions.CourseNotFoundException;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.course.dtos.CourseSummaryResponse;
import ro.mycode.sebischool.course.mapper.CourseMapper;

import java.util.List;

@Component
public class CourseQueryServiceImpl implements CourseQueryService{
    CourseRepository courseRepository;
    public CourseQueryServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;

    }


    @Override
    public List<CourseSummaryResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(CourseMapper::toDto).toList();    }

    @Override
    public CourseSummaryResponse getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(CourseMapper::toDto)
                .orElseThrow(() -> new CourseNotFoundException());

    }

    @Override
    public CourseSummaryResponse getCourseByName(String name) {
        return courseRepository.findByName(name)
                .map(CourseMapper::toDto).orElseThrow(()-> new CourseNotFoundException());
    }

    @Override
    public List<CourseSummaryResponse> getAllCoursesWithEnrollment() {
        return courseRepository.findAllByEnrolmentsIsNotEmpty().stream().map(CourseMapper::toDto).toList();
    }
}
