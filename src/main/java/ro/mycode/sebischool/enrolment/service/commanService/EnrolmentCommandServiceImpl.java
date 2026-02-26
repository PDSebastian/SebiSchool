package ro.mycode.sebischool.enrolment.service.commanService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.course.exceptions.CourseNotFoundException;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.enrolment.exceptions.EnrolmentNotFoundException;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.repository.EnrolmentRepository;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentResponse;
import ro.mycode.sebischool.enrolment.service.mappers.EnrolmentMapper;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;

import java.time.LocalDateTime;

@Component
public class EnrolmentCommandServiceImpl implements EnrolmnetCommandService {
    EnrolmentRepository enrolmentRepository;
    EnrolmentMapper enrolmentMapper;
    StudentRepository studentRepository;
    CourseRepository courseRepository;

   public EnrolmentCommandServiceImpl(EnrolmentRepository enrolmentRepository, EnrolmentMapper enrolmentMapper, StudentRepository studentRepository,CourseRepository courseRepository) {
       this.enrolmentRepository = enrolmentRepository;
       this.enrolmentMapper = enrolmentMapper;
       this.studentRepository = studentRepository;
       this.courseRepository = courseRepository;
   }

    @Override
    @Transactional
    public EnrolmentResponse addEnrolment(Long studentId, EnrolmentRequest enrolmentRequest) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        Course course = courseRepository.findById(enrolmentRequest.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        Enrolment enrolment = enrolmentMapper.toEntity(enrolmentRequest);
        enrolment.setStudent(student);
        enrolment.setCourse(course);
        enrolment.setCreatedAt(LocalDateTime.now());
        return enrolmentMapper.toDto(enrolmentRepository.save(enrolment));
    }

    @Override
    @Transactional
    public EnrolmentResponse updateEnrolment(Long id, EnrolmentRequest enrolmentRequest) {
        Enrolment e = enrolmentRepository.findById(id)
                .orElseThrow(() -> new EnrolmentNotFoundException("Enrolment not found"));
        e.setCreatedAt(enrolmentRequest.getCreatedAt());
        return enrolmentMapper.toDto(enrolmentRepository.save(e));
    }


   @Override
   @Transactional
    public void deleteEnrolment(Long id) {
       if(!enrolmentRepository.existsById(id)){
           throw new EnrolmentNotFoundException("Enrolment not found");
       }
      enrolmentRepository.deleteById(id);
    }
}
