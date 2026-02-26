package ro.mycode.sebischool.course.service.queryService;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.course.exceptions.CourseNotFoundException;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.course.service.dtos.CourseResponse;
import ro.mycode.sebischool.course.service.mapper.CourseMapper;

import java.util.List;

@Component
public class CourseQueryServiceImpl implements CourseQueryService{
    CourseRepository courseRepository;
    CourseMapper courseMapper;
    public CourseQueryServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;

    }


    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toDto).toList();    }

    @Override
    public CourseResponse getCourseById(int id) {
        return courseRepository.findById(id)
                .map(courseMapper::toDto)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

    }

    @Override
    public CourseResponse getCourseByName(String name) {
        return courseRepository.findByName(name)
                .map(courseMapper::toDto).orElseThrow(()-> new CourseNotFoundException("Course not found"));
    }

    @Override
    public List<CourseResponse> getAllCoursesWithEnrollment() {
        return courseRepository.findAllByEnrolmentsIsNotEmpty().stream().map(courseMapper::toDto).toList();
    }
}
