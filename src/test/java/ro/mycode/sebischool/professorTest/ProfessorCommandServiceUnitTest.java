package ro.mycode.sebischool.professorTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.professor.dtos.ProfessorRequest;
import ro.mycode.sebischool.professor.dtos.ProfessorResponse;
import ro.mycode.sebischool.professor.exceptions.ProfessorNotFoundException;
import ro.mycode.sebischool.professor.model.Professor;
import ro.mycode.sebischool.professor.repository.ProfessorRepository;
import ro.mycode.sebischool.professor.service.ProfessorCommandService;
import ro.mycode.sebischool.professor.service.ProfessorCommandServiceImpl;
import ro.mycode.sebischool.users.model.User;
import ro.mycode.sebischool.users.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * UNIT TEST pentru ProfessorCommandServiceImpl.
 * Service-ul are 3 dependinte (professor, user, course repository) — toate @Mock.
 */
@ExtendWith(MockitoExtension.class)
class ProfessorCommandServiceUnitTest {

    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;

    private ProfessorCommandService professorCommandService;

    @BeforeEach
    void setUp() {
        professorCommandService = new ProfessorCommandServiceImpl(
                professorRepository, userRepository, courseRepository);
    }

    private Professor professorWithUser(Long id) {
        User user = User.builder()
                .firstName("Simona")
                .lastName("Radu")
                .email("simona@scoala.ro")
                .build();
        return Professor.builder()
                .id(id)
                .specialty("Java")
                .departament("IT")
                .yearExperience(5)
                .user(user)
                .courses(new HashSet<>())
                .build();
    }

    @Test
    @DisplayName("updateProfessor: actualizeaza specializarea, departamentul si experienta")
    void updateProfessor_ok() {
        // Arrange
        Long id = 1L;
        Professor professor = professorWithUser(id);
        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        ProfessorRequest request = ProfessorRequest.builder()
                .firstName("Simona")
                .lastName("Radu")
                .specialty("Spring")
                .departament("CS")
                .yearExperience(8)
                .build();

        // Act
        ProfessorResponse response = professorCommandService.updateProfessor(id, request);

        // Assert
        assertEquals("Spring", response.specialty());
        assertEquals("CS", response.departament());
        assertEquals(8, response.yearExperience());
    }

    @Test
    @DisplayName("updateProfessor: arunca ProfessorNotFoundException cand id-ul nu exista")
    void updateProfessor_notFound_throws() {
        // Arrange
        Long id = 99L;
        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        ProfessorRequest request = ProfessorRequest.builder()
                .firstName("X")
                .lastName("Y")
                .specialty("Spring")
                .departament("CS")
                .yearExperience(8)
                .build();

        // Act + Assert
        assertThrows(ProfessorNotFoundException.class,
                () -> professorCommandService.updateProfessor(id, request));
    }

    @Test
    @DisplayName("assignCourse: leaga cursul de profesor (ambele parti ale relatiei)")
    void assignCourse_ok() {
        // Arrange
        Long professorId = 1L;
        Long courseId = 2L;
        Professor professor = professorWithUser(professorId);
        Course course = Course.builder().id(courseId).name("Java").departament("IT").build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Act
        professorCommandService.assignCourse(professorId, courseId);

        // Assert — cursul are acum profesorul setat, iar profesorul are cursul in colectie
        assertSame(professor, course.getProfessor());
        assertEquals(1, professor.getCourses().size());
    }
}
