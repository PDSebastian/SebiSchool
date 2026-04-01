package ro.mycode.sebischool.course.service.commandService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.course.exceptions.CourseAlreadyExistsException;
import ro.mycode.sebischool.course.exceptions.CourseFullException;
import ro.mycode.sebischool.course.exceptions.CourseNotFoundException;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.course.dtos.CoursePatchRequest;
import ro.mycode.sebischool.course.dtos.CourseRequest;
import ro.mycode.sebischool.course.dtos.CourseSummaryResponse;
import ro.mycode.sebischool.course.mapper.CourseMapper;
import ro.mycode.sebischool.enrolment.repository.EnrolmentRepository;

@Component
public class CourseCommandServiceImpl implements CourseCommandService {
    private final EnrolmentRepository enrolmentRepository;
    CourseRepository courseRepository;
    public CourseCommandServiceImpl(CourseRepository courseRepository, EnrolmentRepository enrolmentRepository) {
        this.courseRepository = courseRepository;
        this.enrolmentRepository = enrolmentRepository;
    }

    @Override
    @Transactional
    public CourseSummaryResponse addCourse(CourseRequest courseRequest) {
        if (courseRepository.existsByName(courseRequest.getName())) {
            throw new CourseAlreadyExistsException();
        }
        int courseEnrollment= enrolmentRepository.countByCourseId(courseRequest.getId());
        if (courseEnrollment > 2) {
            throw new CourseFullException();
        }
        Course course = CourseMapper.toEntity(courseRequest);
        Course savedCourse = courseRepository.save(course);
        return CourseMapper.toDto(savedCourse);
    }

    @Override
    @Transactional
    public CourseSummaryResponse deleteCourse(Long id) {
        if(!courseRepository.existsById(id)) {
            throw new CourseNotFoundException();
        }
        courseRepository.deleteById(id);
        return null;
    }

    @Override
    @Transactional
    public CourseSummaryResponse updateCourse(Long id, CourseRequest courseRequest) {
        Course course = courseRepository.findById(courseRequest.getId()).orElseThrow(()-> new CourseNotFoundException());


        course.setName(courseRequest.getName());
        course.setDepartament(courseRequest.getDepartament());
        courseRepository.save(CourseMapper.toEntity(courseRequest));
        return CourseMapper.toDto(courseRepository.save(CourseMapper.toEntity(courseRequest)));
    }

    @Override
    @Transactional
    public CourseSummaryResponse updatePatchCourse(Long id, CoursePatchRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException());
        if (request.name() != null ) {
            course.setName(request.name());
        }
        if (request.departament() != null ) {
            course.setDepartament(request.departament());
        }
        Course savedCourse = courseRepository.save(course);
        return CourseMapper.toDto(savedCourse);
    }
}
