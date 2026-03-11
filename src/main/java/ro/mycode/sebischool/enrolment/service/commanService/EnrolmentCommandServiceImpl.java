package ro.mycode.sebischool.enrolment.service.commanService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.course.exceptions.CourseNotFoundException;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.enrolment.exceptions.EnrolmentNotFoundException;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.repository.EnrolmentRepository;
import ro.mycode.sebischool.enrolment.service.dtos.EnrolmentPatchRequest;
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
    public EnrolmentResponse addEnrolment(EnrolmentRequest enrolmentRequest) {
        Student student = studentRepository.findById(enrolmentRequest.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Studentul nu a fost găsit"));

        Course course = courseRepository.findById(enrolmentRequest.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Cursul cu ID-ul nu a fost găsit"));

        Enrolment enrolment = new Enrolment();
        enrolment.setStudent(student);
        enrolment.setCourse(course);
        enrolment.setCreatedAt(LocalDateTime.now());
        Enrolment savedEnrolment = enrolmentRepository.save(enrolment);
        return enrolmentMapper.toDto(savedEnrolment);
    }

    @Override
    @Transactional
    public EnrolmentResponse updateEnrolment(Long id, EnrolmentRequest enrolmentRequest) {
        Enrolment e = enrolmentRepository.findById(id)
                .orElseThrow(() -> new EnrolmentNotFoundException("Enrolment not found"));
        return enrolmentMapper.toDto(enrolmentRepository.save(e));
    }


   @Override
   @Transactional
    public EnrolmentResponse deleteEnrolment(Long id) {
       if(!enrolmentRepository.existsById(id)){
           throw new EnrolmentNotFoundException("Enrolment not found");
       }
      enrolmentRepository.deleteById(id);
       return null;
   }

    @Override
    @Transactional
    public EnrolmentResponse patchEnrolment(Long id, EnrolmentPatchRequest enrolmentRequest) {
        Enrolment enrolment = enrolmentRepository.findById(id)
                .orElseThrow(() -> new EnrolmentNotFoundException("Înscrierea nu a fost găsită"));
        if(enrolmentRequest.studentId()!=null){
            Student student = studentRepository.findById(enrolmentRequest.studentId())
                    .orElseThrow(() -> new StudentNotFoundException("Studentul nou nu există"));
            enrolment.setStudent(student);

        }
        if(enrolmentRequest.courseId()!=null){
            Course course = courseRepository.findById(enrolmentRequest.courseId())
                    .orElseThrow(() -> new CourseNotFoundException("Cursul nou nu există"));
            enrolment.setCourse(course);


        }
        return enrolmentMapper.toDto(enrolmentRepository.save(enrolment));

    }
}
