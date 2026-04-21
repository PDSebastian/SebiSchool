package ro.mycode.sebischool.studentTest;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.core.SubstringMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.student.service.queryService.StudentQueryService;
import ro.mycode.sebischool.student.service.queryService.StudentQueryServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class StudentQueryServiceImplTest {
    @Mock
    StudentQueryService studentQueryService;
    @Mock
    StudentRepository studentRepository;

    @BeforeEach
    public void setup(){
        studentQueryService=new StudentQueryServiceImpl(studentRepository);
    }
    @Test
    void testGetAllStudents(){
        Student student = Student.builder().firstName("John").lastName("Doe").build();
        Student student1 = Student.builder().firstName("John").lastName("Doe").build();
        when(studentRepository.findAll()).thenReturn(List.of(student,student1));
        List<StudentSummaryResponse> list = studentQueryService.getAllStudents();
        assertEquals(2,list.size());
    }

}
