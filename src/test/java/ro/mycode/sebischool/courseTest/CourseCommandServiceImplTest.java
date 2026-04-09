package ro.mycode.sebischool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.mycode.sebischool.course.dtos.CoursePatchRequest;
import ro.mycode.sebischool.course.dtos.CourseRequest;
import ro.mycode.sebischool.course.dtos.CourseSummaryResponse;
import ro.mycode.sebischool.course.exceptions.CourseAlreadyExistsException;
import ro.mycode.sebischool.course.exceptions.CourseFullException;
import ro.mycode.sebischool.course.exceptions.CourseNotFoundException;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.course.service.commandService.CourseCommandService;
import ro.mycode.sebischool.course.service.commandService.CourseCommandServiceImpl;
import ro.mycode.sebischool.enrolment.repository.EnrolmentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CourseCommandServiceImplTest {
    @Mock
    CourseRepository  courseRepository;
    CourseCommandService courseCommandService;
    @Mock
    EnrolmentRepository enrolmentRepository;

    @BeforeEach
    void setUp() {
        courseCommandService = new CourseCommandServiceImpl(courseRepository, enrolmentRepository);

    }
    @Test
    void testAddCourseWhenNameAlreadyExists() {
        CourseRequest request = CourseRequest.builder().name("Java").build();
        when(courseRepository.existsByName("Java")).thenReturn(true);

        assertThrows(CourseAlreadyExistsException.class, () -> courseCommandService.addCourse(request));
    }
    @Test
    void testAddCourseWhenCourseIsFull() {
        CourseRequest request = CourseRequest.builder().name("Java").id(2L).build();
        when(courseRepository.existsByName("Java")).thenReturn(false);
        when(enrolmentRepository.countByCourseId(2L)).thenReturn(3);
        assertThrows(CourseFullException.class, () -> courseCommandService.addCourse(request));
    }
    @Test
    void deleteCourseWhenCourseNotExists() {
        Long id = 2L;
        when(courseRepository.existsById(id)).thenReturn(false);
        assertThrows(CourseNotFoundException.class, () -> courseCommandService.deleteCourse(id));
    }
    @Test
    void testDeleteCourseSuccess() {
        Long id = 2L;
        when(courseRepository.existsById(id)).thenReturn(true);
        courseCommandService.deleteCourse(id);
    }
    @Test
    void updateCourseSuccess() {
       Long id = 3L;
//ce intra si ce iese
        CourseRequest request = CourseRequest.builder().
                id(id)
                .name("AAA")
                .departament("bbb")
                .build();
        CourseSummaryResponse response = CourseSummaryResponse.builder()
                .name("A")
                .department("B")
                .students(null)
                .build();

        Course course = Course.builder()
                .id(id)
                .name("Computer Science")
                .departament("Operating Systems")
                .build();
        Course savedCourse=Course.builder().name("AAA").departament("bbb").build();

        when (courseRepository.findById(id)).thenReturn(Optional.of(course));
        when(courseRepository.save(savedCourse)).thenReturn(course);
        CourseSummaryResponse summaryResponse=courseCommandService.updateCourse(id, request);
        assertEquals("AAA",summaryResponse.getName());
        assertEquals("bbb",summaryResponse.getDepartment());






    }
    @Test
    void updatePatchCourseSuccess() {
        Long id = 2L;
        Course existingCourse = Course.builder()
                .id(id)
                .departament("IT")
                .name("J")
                .build();

        CoursePatchRequest patchRequest = new CoursePatchRequest("name", null);
        when(courseRepository.findById(id)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);
        CourseSummaryResponse response = courseCommandService.updatePatchCourse(id, patchRequest);
        assertNotNull(response);
        assertEquals("name", response.getName());

    }
    @Test
    void testUpdateCourseNotFound() {
        Long id = 2L;
        CourseRequest request = CourseRequest.builder()
                .id(id)
                .name("New Name")
                .departament("IT")
                .build();
        when(courseRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> {
            courseCommandService.updateCourse(id, request);
        });


    }




    }



