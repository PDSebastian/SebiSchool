package ro.mycode.sebischool.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ro.mycode.sebischool.enrolment.controller.EnrolmentController;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;
import ro.mycode.sebischool.enrolment.service.commanService.EnrolmentCommandService;
import ro.mycode.sebischool.enrolment.service.queryService.EnrolmentQueryService;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EnrolmentController.class)
public class enrollmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EnrolmentCommandService enrolmentCommandService;

    @MockBean
    private EnrolmentQueryService enrolmentQueryService;

    @Test
    void addEnrolmentReturnsOk() throws Exception {
        Long studentId = 2L;
        Long courseId = 3L;

        EnrolmentRequest request = EnrolmentRequest.builder()
                .studentId(studentId)
                .courseId(courseId)
                .build();

        EnrolmentResponse response = EnrolmentResponse.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .build();

        when(enrolmentCommandService.addEnrolment(any(EnrolmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v2/enrolment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.createdAt").exists());

    }
    @Test
    void deleteEnrolmentReturnsOk() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/v2/enrolment/delete/{id}", id))
                .andExpect(status().isOk());

    }
    @Test
    void getAllEnrolmentsReturnsList() throws Exception {
        Long id=1L;
        EnrolmentResponse enrolment = EnrolmentResponse.builder()
                .id(id)
                .build();

        when(enrolmentQueryService.getAllEnrolments()).thenReturn(List.of(enrolment));

        mockMvc.perform(get("/api/v2/enrolment/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));

    }
    @Test
    void getStudentsByCourseIdReturnsList() throws Exception {
        Long courseId = 3L;
        StudentSummaryResponse student = StudentSummaryResponse.builder()
                .id(10L)
                .firstName("Sebastian")
                .lastName("Popescu")
                .build();

        when(enrolmentQueryService.getStudentsByCourseId(courseId))
                .thenReturn(List.of(student));

        mockMvc.perform(get("/api/v2/enrolment/course/{courseId}/students", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].firstName").value("Sebastian"));

    }



}
