package ro.mycode.sebischool.integration.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ro.mycode.sebischool.course.controller.CourseController;
import ro.mycode.sebischool.course.dtos.CoursePatchRequest;
import ro.mycode.sebischool.course.dtos.CourseRequest;
import ro.mycode.sebischool.course.dtos.CourseSummaryResponse;
import ro.mycode.sebischool.course.service.commandService.CourseCommandService;
import ro.mycode.sebischool.course.service.queryService.CourseQueryService;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CourseController.class)
public class CourseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourseCommandService courseCommandService;

    @MockitoBean
    private CourseQueryService courseQueryService;


    @Test
    void testAddCourseReturnOk() throws Exception {
        CourseRequest request = CourseRequest.builder()
                .name("mate")
                .departament("stiinte")
                .build();

        CourseSummaryResponse response = CourseSummaryResponse.builder()
                .id(1L)
                .name("mate")
                .department("stiinte")
                .students(List.of())
                .build();

        when(courseCommandService.addCourse(request)).thenReturn(response);

        mockMvc.perform(post("/api/v2/course/add")
                        .contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("mate"));


    }
    @Test
    void testGetCourseByIdReturnOk() throws Exception {
        Long courseId = 1L;
        CourseSummaryResponse response = CourseSummaryResponse.builder()
                .id(courseId)
                .name("Mate")
                .department("Stiinte")
                .students(List.of())
                .build();

        when(courseQueryService.getCourseById(courseId)).thenReturn(response);

        mockMvc.perform(get("/api/v2/course/" + courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseId))
                .andExpect(jsonPath("$.name").value("Mate"));


    }
    @Test
    void testGetCourseByNameReturnOk() throws Exception {
        Long courseId = 2L;
        String courseName = "Istorie";
        CourseSummaryResponse response = CourseSummaryResponse.builder()
                .id(courseId)
                .name(courseName)
                .department("Uman")
                .students(List.of())
                .build();

        when(courseQueryService.getCourseByName(courseName)).thenReturn(response);

        mockMvc.perform(get("/api/v2/course/name/" + courseName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(courseName));


    }
    @Test
    void testUpdateCourseReturnOk() throws Exception {
        Long courseId = 1L;
        CourseRequest updateRequest = CourseRequest.builder()
                .name("Mate ")
                .departament("Stiinte")
                .build();

        CourseSummaryResponse response = CourseSummaryResponse.builder()
                .id(courseId)
                .name("Mate ")
                .department("Stiinte")
                .students(List.of())
                .build();

        when(courseCommandService.updateCourse(courseId, updateRequest)).thenReturn(response);

        mockMvc.perform(put("/api/v2/course/update/" + courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mate "));


    }
    @Test
    void testDeleteCourseReturnOk() throws Exception {
        Long courseId = 1L;
        CourseSummaryResponse response = CourseSummaryResponse.builder()
                .id(courseId)
                .name("Curs Sters")
                .students(List.of())
                .build();

        when(courseCommandService.deleteCourse(courseId)).thenReturn(response);

        mockMvc.perform(delete("/api/v2/course/delete/" + courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseId));


    }
    @Test
    void testPatchCourseReturnOk() throws Exception {
        Long courseId = 1L;
        String courseName = "Nume nou";
        String courseDepartment = "Stiinte";
        CoursePatchRequest patchRequest = CoursePatchRequest.builder()
                .name(courseName)
                .build();

        CourseSummaryResponse response = CourseSummaryResponse.builder()
                .id(courseId)
                .name(courseName)
                .department(courseDepartment)
                .students(List.of())
                .build();

        when(courseCommandService.updatePatchCourse(courseId, patchRequest)).thenReturn(response);

        mockMvc.perform(patch("/api/v2/course/patch/" + courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(courseName));

    }



}
