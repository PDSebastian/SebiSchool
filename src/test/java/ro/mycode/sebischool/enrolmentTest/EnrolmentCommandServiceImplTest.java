package ro.mycode.sebischool.enrolmentTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentPatchRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;
import ro.mycode.sebischool.enrolment.exceptions.EnrolmentNotFoundException;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.repository.EnrolmentRepository;
import ro.mycode.sebischool.enrolment.service.commanService.EnrolmentCommandServiceImpl;
import ro.mycode.sebischool.enrolment.service.commanService.EnrolmentCommandService;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class EnrolmentCommandServiceImplTest {
    @Mock
    EnrolmentRepository  enrolmentRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    CourseRepository courseRepository;

    EnrolmentCommandService enrolmentCommandService;

    @BeforeEach
    public void setup() {
        enrolmentCommandService= new EnrolmentCommandServiceImpl(enrolmentRepository,studentRepository,courseRepository);
    }
    @Test
    void testDeleteEnrolment_Success() {
        Long id = 10L;
        when(enrolmentRepository.existsById(id)).thenReturn(true);

        enrolmentCommandService.deleteEnrolment(id);

    }

    @Test
    void testDeleteEnrolment_NotFound() {
        Long id = 99L;
        when(enrolmentRepository.existsById(id)).thenReturn(false);

        assertThrows(EnrolmentNotFoundException.class, () -> enrolmentCommandService.deleteEnrolment(id));
    }
    @Test
    void testAddEnrolmentSuccess() {
        Long studentId = 1L;
        Long courseId = 2L;
        Long enrolmentId = 100L;

        EnrolmentRequest enrolmentRequest = EnrolmentRequest.builder()
                .studentId(studentId)
                .courseId(courseId)
                .build();

        Student student = Student.builder().id(studentId).firstName("Andrei").build();
        Course course = Course.builder().id(courseId).name("Java").build();

        Enrolment savedEnrolment = Enrolment.builder()
                .id(enrolmentId)
                .student(student)
                .course(course)
                .createdAt(LocalDateTime.now())
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        when(enrolmentRepository.existsByStudentIdAndCourseId(studentId, courseId)).thenReturn(false);

        when(enrolmentRepository.save(any(Enrolment.class))).thenReturn(savedEnrolment);
        EnrolmentResponse response = enrolmentCommandService.addEnrolment(enrolmentRequest);


        assertEquals(enrolmentId, response.getId());

    }
    @Test
    void testUpdateEnrolmentSuccess() {
        Long enrolmentId = 10L;
        Long studentId = 5L;
        Long courseId = 5L;
        Enrolment existingEnrolment = Enrolment.builder()
                .id(enrolmentId)
                .build();

        EnrolmentRequest request = EnrolmentRequest.builder()
                .studentId(studentId)
                .courseId(courseId)
                .build();


        when(enrolmentRepository.findById(enrolmentId)).thenReturn(Optional.of(existingEnrolment));
        when(enrolmentRepository.save(any(Enrolment.class))).thenReturn(existingEnrolment);
        EnrolmentResponse response = enrolmentCommandService.updateEnrolment(enrolmentId, request);


        assertEquals(enrolmentId, response.getId());
    }
    @Test
    void testPatchEnrolmentSuccess() {
        Long enrolmentId = 10L;
        Long newStudentId = 5L;
        Long newCourseId = 5L;

        Enrolment existingEnrolment = Enrolment.builder().id(enrolmentId).build();
        Student newStudent = Student.builder().id(newStudentId).firstName("sdsdsds").build();
        Course newCourse = Course.builder().id(newCourseId).name("dsdsd").build();

        EnrolmentPatchRequest patchRequest = new EnrolmentPatchRequest(newStudentId, newCourseId);

        when(enrolmentRepository.findById(enrolmentId)).thenReturn(Optional.of(existingEnrolment));
        when(studentRepository.findById(newStudentId)).thenReturn(Optional.of(newStudent));
        when(courseRepository.findById(newCourseId)).thenReturn(Optional.of(newCourse));
        when(enrolmentRepository.save(any(Enrolment.class))).thenReturn(existingEnrolment);

        EnrolmentResponse response = enrolmentCommandService.patchEnrolment(enrolmentId, patchRequest);
        assertEquals(enrolmentId, response.getId());


    }




}
