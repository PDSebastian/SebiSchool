package ro.mycode.sebischool.studentTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.mycode.sebischool.student.dtos.StudentRequest;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.exceptions.InvalidStudentAgeException;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.student.service.commandService.StudentCommandServiceImpl;
import ro.mycode.sebischool.users.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * UNIT TEST — testeaza DOAR logica din StudentCommandServiceImpl, izolat.
 *
 * Caracteristici:
 *  - fara Spring (nu porneste contextul) -> ruleaza in milisecunde
 *  - repository-ul e FALS (@Mock) -> nu atinge nicio baza de date reala
 *  - controlam noi ce returneaza repo-ul prin when(...).thenReturn(...)
 *
 * Cand folosesti unit test: cand vrei sa verifici reguli de business
 * (validari, exceptii, ce metode se apeleaza) fara costul unui DB real.
 */
@ExtendWith(MockitoExtension.class)
class StudentCommandServiceUnitTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentCommandServiceImpl studentCommandService;

    @BeforeEach
    void setUp() {
        // construim manual service-ul si ii injectam repo-ul fals
        studentCommandService = new StudentCommandServiceImpl(studentRepository);
    }

    @Test
    @DisplayName("updateStudent: actualizeaza campurile si intoarce noile valori")
    void updateStudent_ok() {
        // Arrange
        Long id = 1L;
        User user = User.builder()
                .firstName("Andrei")
                .lastName("Popescu")
                .email("andrei.popescu@scoala.ro")
                .build();
        Student existing = Student.builder()
                .id(id)
                .age(20)
                .user(user)
                .build();
        when(studentRepository.findById(id)).thenReturn(Optional.of(existing));

        StudentRequest request = StudentRequest.builder()
                .firstName("Andrei")
                .lastName("Ionescu")
                .email("andrei.ionescu@scoala.ro")
                .age(21)
                .build();

        // Act
        StudentSummaryResponse response = studentCommandService.updateStudent(id, request);

        // Assert
        assertEquals("Ionescu", response.getLastName());
        assertEquals("andrei.ionescu@scoala.ro", response.getEmail());
        assertEquals(21, response.getAge());
    }

    @Test
    @DisplayName("updateStudent: arunca StudentNotFoundException cand id-ul nu exista")
    void updateStudent_notFound_throws() {
        // Arrange
        Long id = 99L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        StudentRequest request = StudentRequest.builder()
                .firstName("X")
                .lastName("Y")
                .email("x@scoala.ro")
                .age(21)
                .build();

        // Act + Assert
        assertThrows(StudentNotFoundException.class,
                () -> studentCommandService.updateStudent(id, request));
    }

    @Test
    @DisplayName("updateStudent: arunca InvalidStudentAgeException pentru varsta peste 100")
    void updateStudent_invalidAge_throws() {
        // Arrange
        Long id = 1L;
        User user = User.builder().firstName("Ana").lastName("Pop").email("ana@scoala.ro").build();
        Student existing = Student.builder().id(id).age(20).user(user).build();
        when(studentRepository.findById(id)).thenReturn(Optional.of(existing));

        StudentRequest request = StudentRequest.builder()
                .firstName("Ana")
                .lastName("Pop")
                .email("ana@scoala.ro")
                .age(150)
                .build();

        // Act + Assert
        assertThrows(InvalidStudentAgeException.class,
                () -> studentCommandService.updateStudent(id, request));
    }

    @Test
    @DisplayName("deleteStudent: apeleaza deleteById cand studentul exista")
    void deleteStudent_ok() {
        // Arrange
        Long id = 1L;
        when(studentRepository.existsById(id)).thenReturn(true);

        // Act
        studentCommandService.deleteStudent(id);

        // Assert — verificam ca metoda de stergere chiar a fost apelata
        verify(studentRepository).deleteById(id);
    }

    @Test
    @DisplayName("deleteStudent: arunca StudentNotFoundException si NU sterge nimic")
    void deleteStudent_notFound_throws() {
        // Arrange
        Long id = 99L;
        when(studentRepository.existsById(id)).thenReturn(false);

        // Act + Assert
        assertThrows(StudentNotFoundException.class,
                () -> studentCommandService.deleteStudent(id));
        verify(studentRepository, never()).deleteById(id);
    }
}
