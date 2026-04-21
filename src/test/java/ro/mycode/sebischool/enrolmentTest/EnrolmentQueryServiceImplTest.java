package ro.mycode.sebischool.enrolmentTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.repository.EnrolmentRepository;
import ro.mycode.sebischool.enrolment.service.queryService.EnrolmentQueryServiceImpl;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.model.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class EnrolmentQueryServiceImplTest {
    @Mock
    EnrolmentRepository enrolmentRepository;
    EnrolmentQueryServiceImpl enrolmentQueryServiceImpl;

    @BeforeEach
    public void setup() {
        enrolmentQueryServiceImpl=new EnrolmentQueryServiceImpl(enrolmentRepository);
    }
    @Test
    void testGetAllEnrolments(){
        Enrolment e1 = Enrolment.builder().id(1L).build();
        Enrolment e2 = Enrolment.builder().id(2L).build();
        when(enrolmentRepository.findAll()).thenReturn(List.of(e1, e2));

        List<EnrolmentResponse> result = enrolmentQueryServiceImpl.getAllEnrolments();
        assertEquals(2, result.size());
    }
    @Test
    void testGetAllEnrolmentsByCourseId() {
        Long courseId = 5L;
        Course course = Course.builder().id(courseId).name("Java").build();
        Enrolment e1 = Enrolment.builder().id(100L).course(course).build();
        when(enrolmentRepository.findByCourseId(courseId)).thenReturn(List.of(e1));

        List<EnrolmentResponse> result = enrolmentQueryServiceImpl.getAllEnrolmentsByCourseId(courseId);

        assertEquals(1, result.size());

    }
    @Test
    void testGetStudentsByCourseId() {
        Long courseId = 5L;
        Student student = Student.builder()
                .id(1L)
                .firstName("Andrei")
                .lastName("Popescu")
                .email("andrei@yahoo.com")
                .build();

        Enrolment e1 = Enrolment.builder().id(10L).student(student).build();

        when(enrolmentRepository.findByCourseId(courseId)).thenReturn(List.of(e1));

        List<StudentSummaryResponse> result = enrolmentQueryServiceImpl.getStudentsByCourseId(courseId);
        assertEquals(1, result.size());

    }



}
