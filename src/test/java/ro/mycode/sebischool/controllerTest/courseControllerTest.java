package ro.mycode.sebischool.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ro.mycode.sebischool.course.controller.CourseController;
import ro.mycode.sebischool.course.dtos.CourseDetailResponse;
import ro.mycode.sebischool.course.dtos.CourseRequest;
import ro.mycode.sebischool.course.dtos.CourseSummaryResponse;
import ro.mycode.sebischool.course.service.commandService.CourseCommandService;
import ro.mycode.sebischool.course.service.queryService.CourseQueryService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CourseController.class)
public class courseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourseCommandService courseCommandService;

    @MockitoBean
    private CourseQueryService courseQueryService;

    @Test
    void addCourseReturnsOk() throws Exception {
        Long studentId = 1L;

        CourseRequest request = CourseRequest.builder()
                .id(studentId)
                .name("Tttt")
                .departament("IT")
                .build();

        CourseSummaryResponse response = CourseSummaryResponse.builder()
                .id(studentId)
                .name("Tttt")
                .department("IT")
                .students(List.of())
                .build();


        when(courseCommandService.addCourse( any(CourseRequest.class))).thenReturn(response);


        mockMvc.perform(post("/api/v2/course/add", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tttt"));

    }
@Test
void updateCourseReturnsOk() throws Exception {
    Long id = 1L;
    CourseRequest request = CourseRequest.builder()
            .id(id)
            .name("Tttt")
            .departament("IT")
            .build();

    CourseSummaryResponse response = CourseSummaryResponse.builder()
            .id(id)
            .name("Tttt")
            .build();

    when(courseCommandService.updateCourse(eq(id), any(CourseRequest.class)))
            .thenReturn(response);

    mockMvc.perform(put("/api/v2/course/update/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Tttt"));

}
    @Test
    void getAllCoursesReturnsList() throws Exception {
        // GIVEN
        CourseSummaryResponse course = CourseSummaryResponse.builder()
                .id(1L)
                .name("Java")
                .build();

        when(courseQueryService.getAllCourses()).thenReturn(List.of(course));

        mockMvc.perform(get("/api/v2/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Java"));


    }
    @Test
    void getCourseByIdReturnsItem() throws Exception {
        Long id = 1L;
        CourseSummaryResponse response = CourseSummaryResponse.builder()
                .id(id)
                .name("Math")
                .department("Science")
                .students(List.of())
                .build();

        when(courseQueryService.getCourseById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v2/course/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Math"));

    }
    @Test
    void deleteCourseReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v2/course/delete/{id}", 1L))
                .andExpect(status().isOk());

    }


}
