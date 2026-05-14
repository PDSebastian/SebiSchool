package ro.mycode.sebischool.integration.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ro.mycode.sebischool.enrolment.controller.EnrolmentController;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentPatchRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;
import ro.mycode.sebischool.enrolment.service.commanService.EnrolmentCommandService;
import ro.mycode.sebischool.enrolment.service.queryService.EnrolmentQueryService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EnrolmentController.class)
public class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EnrolmentCommandService enrolmentCommandService;

    @MockitoBean
    private EnrolmentQueryService enrolmentQueryService;

    @Test
    void testAddEnrolmentReturnsCreated() throws Exception {
        Long studentId = 1L;
        Long courseId = 2L;
        EnrolmentRequest request = EnrolmentRequest.builder()
                .studentId(studentId)
                .courseId(courseId)
                .build();

        EnrolmentResponse response = EnrolmentResponse.builder()
                .id(100L)
                .createdAt(LocalDateTime.now())
                .build();

        when(enrolmentCommandService.addEnrolment(request)).thenReturn(response);

        mockMvc.perform(post("/api/v2/enrolment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L));


    }
    @Test
    void testDeleteEnrolmentReturnsOk() throws Exception {
        Long id = 1L;
        EnrolmentResponse response = EnrolmentResponse.builder().id(id).build();
        when(enrolmentCommandService.deleteEnrolment(id)).thenReturn(response);

        mockMvc.perform(delete("/api/v2/enrolment/delete/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));


    }
    @Test
    void testUpdateEnrolmentReturnsOk() throws Exception {
        Long id = 1L;
        EnrolmentRequest request = EnrolmentRequest.builder().studentId(5L).courseId(10L).build();
        EnrolmentResponse response = EnrolmentResponse.builder().id(id).build();

        when(enrolmentCommandService.updateEnrolment(id, request)).thenReturn(response);

        mockMvc.perform(put("/api/v2/enrolment/update/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }
    @Test
    void testPatchEnrolmentReturnsOk() throws Exception {
        Long id = 1L;
        EnrolmentPatchRequest request = EnrolmentPatchRequest.builder().courseId(99L).build();
        EnrolmentResponse response = EnrolmentResponse.builder().id(id).build();

        when(enrolmentCommandService.patchEnrolment(id, request)).thenReturn(response);

        mockMvc.perform(patch("/api/v2/enrolment/patch/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());


    }
    @Test
    void testGetEnrolmentsByCourseIdReturnsList() throws Exception {
        Long courseId = 2L;
        EnrolmentResponse response = EnrolmentResponse.builder().id(1L).build();
        when(enrolmentQueryService.getAllEnrolmentsByCourseId(courseId)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v2/enrolment/course/" + courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

    }
    @Test
    void testGetAllEnrolmentsReturnsList() throws Exception {
        EnrolmentResponse enrolment = EnrolmentResponse.builder().id(1L).build();
        when(enrolmentQueryService.getAllEnrolments()).thenReturn(List.of(enrolment));

        mockMvc.perform(get("/api/v2/enrolment/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));


    }





}
