package ro.mycode.sebischool.studentTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ro.mycode.sebischool.student.dtos.StudentPatchRequest;
import ro.mycode.sebischool.student.dtos.StudentRequest;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.exceptions.InvalidStudentAgeException;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.student.service.commandService.StudentCommandServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class StudentCommandServiceImplTest {

    @Mock
    StudentRepository studentRepository;
    StudentCommandServiceImpl studentCommandServiceImpl;

    @BeforeEach
    void setUp() {
       studentCommandServiceImpl=new StudentCommandServiceImpl(studentRepository);
    }
    @Test
    void testAddStudent() {
        String email = "andrei@yahoo.com";
        StudentRequest studentRequest = StudentRequest.builder()
                .firstName("Andrei")
                .lastName("Popescu")
                .email(email)
                .age(20)
                .build();
        Student student = Student.builder()
                .firstName("Andrei")
                .lastName("Popescu")
                .email(email)
                .age(20)
                .build();
        Student savedStudent = Student.builder()
                .id(1L)
                .firstName("Andrei")
                .lastName("Popescu")
                .email(email)
                .age(20)
                .build();
        StudentSummaryResponse expectedResponse = StudentSummaryResponse.builder()
                .id(1L)
                .firstName("Andrei")
                .lastName("Popescu")
                .email(email)
                .age(20)
                .build();

       when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(studentRepository.save(student)).thenReturn(savedStudent);
        StudentSummaryResponse actualResponse = studentCommandServiceImpl.addStudent(studentRequest);
        assertEquals(expectedResponse, actualResponse);
    }
    @Test
    void testDeleteStudent() {
        Long id = 100L;

        when(studentRepository.existsById(id)).thenReturn(true);
        studentCommandServiceImpl.deleteStudent(id);


    }
    @Test
    void testDeleteStudentException() {
        Long id = 100L;

        when(studentRepository.existsById(id)).thenReturn(false);
        assertThrows(StudentNotFoundException.class, () -> {
            studentCommandServiceImpl.deleteStudent(id);
        });


    }
    @Test
    void testUpdateStudent() {

        String email = "andrei@yahoo.com";
        StudentRequest studentRequest = StudentRequest.builder()
                .firstName("Andrei")
                .lastName("Popescu")
                .email(email)
                .age(21)
                .build();

        Student existingStudent = Student.builder()
                .id(1L)
                .firstName("Andrei")
                .lastName("Popescu")
                .email(email)
                .age(20)
                .build();

        Student savedStudent = Student.builder()
                .id(1L)
                .firstName("Andrei")
                .lastName("Popescu-Modificat")
                .email(email)
                .age(21)
                .build();

        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(org.mockito.ArgumentMatchers.any(Student.class))).thenReturn(savedStudent);

        StudentSummaryResponse result = studentCommandServiceImpl.updateStudent(studentRequest);

        assertEquals("Popescu", result.getLastName());
        assertEquals(1L, result.getId());
    }
    @Test
    void testUpdatePatchStudent() {
        Long studentId = 1L;

        StudentPatchRequest patchRequest = new StudentPatchRequest(
                "A", null, "dwdwd@gmail",25);

        Student existingStudent = Student.builder()
                .id(studentId)
                .firstName("Andrei")
                .lastName("Popescu")
                .email("andrei@yahoo.com")
                .age(20)
                .build();

        Student savedStudent = Student.builder()
                .id(studentId)
                .firstName("Andrei-Nou")
                .email("andrei@yahoo.com")
                .age(25)
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(org.mockito.ArgumentMatchers.any(Student.class))).thenReturn(savedStudent);

        StudentSummaryResponse result = studentCommandServiceImpl.updatePatchStudent(studentId, patchRequest);
        assertEquals("A", result.getFirstName());
        assertEquals("Popescu", result.getLastName());
        assertEquals(25, result.getAge());
        assertEquals(studentId, result.getId());
    }
    @Test
    void addStudentInvalidAge() {
        String email = "andrei@yahoo.com";
        StudentRequest studentRequest = StudentRequest.builder()
                .firstName("Andrei")
                .lastName("Popescu")
                .email(email)
                .age(105)
                .build();
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(InvalidStudentAgeException.class, () -> {
            studentCommandServiceImpl.addStudent(studentRequest);
        });
    }




}
