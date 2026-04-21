package ro.mycode.sebischool.courseTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.mycode.sebischool.course.dtos.CourseSummaryResponse;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.course.service.queryService.CourseQueryService;
import ro.mycode.sebischool.course.service.queryService.CourseQueryServiceImpl;
import ro.mycode.sebischool.enrolment.model.Enrolment;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CourseQueryServiceImplTest {
    @Mock
    CourseQueryService courseQueryService;
    @Mock
    CourseRepository courseRepository;

    @BeforeEach
    public void setup(){
        courseQueryService=new CourseQueryServiceImpl(courseRepository);
    }
    @Test
    void testGetAllCourses(){
        Long id=1L;
        Long id2=2L;
        Course course=Course.builder().id(id).build();
        Course course2=Course.builder().id(id2).build();
        when(courseRepository.findAll()).thenReturn(List.of(course,course2));
        List<CourseSummaryResponse> list=courseQueryService.getAllCourses();
        assertEquals(2,list.size());

    }
    @Test
    void testGetCourseById(){
        Long id=1L;
        Course course=Course.builder().id(id).build();
        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        CourseSummaryResponse courseSummaryResponse=courseQueryService.getCourseById(id);
        assertEquals(id,courseSummaryResponse.getId());



    }
    @Test
    void testGetCourseByName(){
        Long id=4L;
        String name="Computer Networks";
        Course course=Course.builder().id(id).name(name).build();
        when(courseRepository.findByName(name)).thenReturn(Optional.of(course));
        CourseSummaryResponse courseSummaryResponse=courseQueryService.getCourseByName(name);
        assertEquals(name,courseSummaryResponse.getName());
    }
    @Test
    void testGetCourseByEnrolment(){
        Long enrollmenId=1L;
        Enrolment enrolment=Enrolment.builder().id(enrollmenId).build();
        Long id=3L;
        Course course=Course.builder().id(id).enrolments(List.of(enrolment)).build();
       when(courseRepository.findAllByEnrolmentsIsNotEmpty()).thenReturn(List.of(course));
       List<CourseSummaryResponse> list=courseQueryService.getAllCoursesWithEnrollment();
        assertEquals(1,list.size());

    }


}
