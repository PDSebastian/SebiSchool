package ro.mycode.sebischool.enrolmentTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;
import ro.mycode.sebischool.enrolment.exceptions.EnrolmentAlreadyExistsException;
import ro.mycode.sebischool.enrolment.exceptions.EnrolmentNotFoundException;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.repository.EnrolmentRepository;
import ro.mycode.sebischool.enrolment.service.commandService.EnrolmentCommandService;
import ro.mycode.sebischool.enrolment.service.commandService.EnrolmentCommandServiceImpl;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * UNIT TEST pentru EnrolmentCommandServiceImpl.
 *
 * Service-ul depinde de 3 repository-uri (enrolment, student, course) — toate FALSE (@Mock).
 * Important: cand metoda reala apeleaza mai multe repo-uri, trebuie sa stub-uim
 * TOATE apelurile, altfel un Optional.empty() neasteptat arunca o exceptie.
 */
@ExtendWith(MockitoExtension.class)
class EnrolmentCommandServiceImplTest {

    @Mock
    private EnrolmentRepository enrolmentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;

    private EnrolmentCommandService enrolmentCommandService;

    @BeforeEach
    void setUp() {
        enrolmentCommandService = new EnrolmentCommandServiceImpl(
                enrolmentRepository, studentRepository, courseRepository);
    }

    @Test
    @DisplayName("addEnrolment: leaga student + course si salveaza inscrierea")
    void addEnrolment_ok() {
        // Arrange
        Long studentId = 1L;
        Long courseId = 2L;
        Student student = Student.builder().id(studentId).age(20).build();
        Course course = Course.builder().id(courseId).name("Java").build();
        EnrolmentRequest request = EnrolmentRequest.builder()
                .studentId(studentId)
                .courseId(courseId)
                .build();
        Enrolment saved = Enrolment.builder().id(100L).student(student).course(course).build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrolmentRepository.existsByStudentIdAndCourseId(studentId, courseId)).thenReturn(false);
        when(enrolmentRepository.save(any(Enrolment.class))).thenReturn(saved);

        // Act
        EnrolmentResponse response = enrolmentCommandService.addEnrolment(request);

        // Assert
        assertEquals(100L, response.getId());
    }

    @Test
    @DisplayName("addEnrolment: arunca EnrolmentAlreadyExistsException daca inscrierea exista deja")
    void addEnrolment_alreadyExists_throws() {
        // Arrange
        Long studentId = 1L;
        Long courseId = 2L;
        Student student = Student.builder().id(studentId).age(20).build();
        Course course = Course.builder().id(courseId).name("Java").build();
        EnrolmentRequest request = EnrolmentRequest.builder()
                .studentId(studentId)
                .courseId(courseId)
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrolmentRepository.existsByStudentIdAndCourseId(studentId, courseId)).thenReturn(true);

        // Act + Assert
        assertThrows(EnrolmentAlreadyExistsException.class,
                () -> enrolmentCommandService.addEnrolment(request));
        verify(enrolmentRepository, never()).save(any(Enrolment.class));
    }

    @Test
    @DisplayName("updateEnrolment: re-leaga student + course pe inscrierea existenta")
    void updateEnrolment_ok() {
        // Arrange
        Long enrolmentId = 10L;
        Long studentId = 5L;
        Long courseId = 7L;
        Enrolment existing = Enrolment.builder().id(enrolmentId).build();
        Student student = Student.builder().id(studentId).age(22).build();
        Course course = Course.builder().id(courseId).name("Spring").build();
        EnrolmentRequest request = EnrolmentRequest.builder()
                .studentId(studentId)
                .courseId(courseId)
                .build();

        when(enrolmentRepository.findById(enrolmentId)).thenReturn(Optional.of(existing));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrolmentRepository.save(any(Enrolment.class))).thenReturn(existing);

        // Act
        EnrolmentResponse response = enrolmentCommandService.updateEnrolment(enrolmentId, request);

        // Assert
        assertEquals(enrolmentId, response.getId());
    }

    @Test
    @DisplayName("updateEnrolment: arunca EnrolmentNotFoundException cand inscrierea nu exista")
    void updateEnrolment_notFound_throws() {
        // Arrange
        Long enrolmentId = 99L;
        EnrolmentRequest request = EnrolmentRequest.builder()
                .studentId(1L)
                .courseId(2L)
                .build();
        when(enrolmentRepository.findById(enrolmentId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EnrolmentNotFoundException.class,
                () -> enrolmentCommandService.updateEnrolment(enrolmentId, request));
    }

    @Test
    @DisplayName("deleteEnrolment: apeleaza deleteById cand inscrierea exista")
    void deleteEnrolment_ok() {
        // Arrange
        Long id = 10L;
        when(enrolmentRepository.existsById(id)).thenReturn(true);

        // Act
        enrolmentCommandService.deleteEnrolment(id);

        // Assert
        verify(enrolmentRepository).deleteById(id);
    }

    @Test
    @DisplayName("deleteEnrolment: arunca EnrolmentNotFoundException si NU sterge nimic")
    void deleteEnrolment_notFound_throws() {
        // Arrange
        Long id = 99L;
        when(enrolmentRepository.existsById(id)).thenReturn(false);

        // Act + Assert
        assertThrows(EnrolmentNotFoundException.class,
                () -> enrolmentCommandService.deleteEnrolment(id));
        verify(enrolmentRepository, never()).deleteById(id);
    }
}
