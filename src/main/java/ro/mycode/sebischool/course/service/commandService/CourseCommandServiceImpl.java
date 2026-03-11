package ro.mycode.sebischool.course.service.commandService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.books.exceptions.BookAlreadyExistsException;
import ro.mycode.sebischool.course.exceptions.CourseNotFoundException;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.course.service.dtos.CoursePatchRequest;
import ro.mycode.sebischool.course.service.dtos.CourseRequest;
import ro.mycode.sebischool.course.service.dtos.CourseResponse;
import ro.mycode.sebischool.course.service.mapper.CourseMapper;

@Component
public class CourseCommandServiceImpl implements CourseCommandService {
    CourseRepository courseRepository;
    CourseMapper courseMapper;
    public CourseCommandServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    @Transactional
    public CourseResponse addCourse( CourseRequest courseRequest) {
        courseRepository.findByName(courseRequest.getName())
                .ifPresent(course -> {throw new BookAlreadyExistsException("Course already exists");});
        courseRepository.save(courseMapper.toEntity(courseRequest));
        return courseMapper.toDto(courseRepository.save(courseMapper.toEntity(courseRequest)));
    }

    @Override
    @Transactional
    public CourseResponse deleteCourse(Long id) {
        if(!courseRepository.existsById(id)) {
            throw new CourseNotFoundException("Course not found");
        }
        courseRepository.deleteById(id);
        return null;
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest courseRequest) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        course.setName(courseRequest.getName());
        course.setDepartament(courseRequest.getDepartament());
        courseRepository.save(courseMapper.toEntity(courseRequest));
        return courseMapper.toDto(courseRepository.save(courseMapper.toEntity(courseRequest)));
    }

    @Override
    @Transactional
    public CourseResponse updatePatchCourse(Long id, CoursePatchRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Cursul nu a fost găsit!"));
        if (request.name() != null ) {
            course.setName(request.name());
        }
        if (request.departament() != null ) {
            course.setDepartament(request.departament());
        }
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDto(savedCourse);
    }
}
