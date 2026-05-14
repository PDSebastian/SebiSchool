package ro.mycode.sebischool.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ro.mycode.sebischool.student.controller.StudentController;
import ro.mycode.sebischool.student.dtos.StudentDetailResponse;
import ro.mycode.sebischool.student.dtos.StudentRequest;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;
import ro.mycode.sebischool.student.service.commandService.StudentCommandService;
import ro.mycode.sebischool.student.service.queryService.StudentQueryService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class)
public class studentControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentCommandService studentCommandService;

    @MockBean
    private StudentQueryService studentQueryService;

    @Test
    void addStudentReturnsOk() throws Exception {
        StudentRequest request = StudentRequest.builder()
                .firstName("Andrei")
                .lastName("Ionescu")
                .email("andrei@yahoo.com")
                .age(20)
                .build();

        StudentSummaryResponse response = StudentSummaryResponse.builder()
                .id(1L)
                .firstName("Andrei")
                .lastName("Ionescu")
                .email("andrei@yahoo.com")
                .build();

        when(studentCommandService.addStudent(any(StudentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v2/student/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Andrei"));

    }
    @Test
    void deleteStudentReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v2/student/delete/{id}", 1L))
                .andExpect(status().isOk());

    }
    @Test
    void getAllStudentsReturnsList() throws Exception {
        StudentSummaryResponse student = StudentSummaryResponse.builder()
                .id(1L)
                .firstName("Elena")
                .build();

        when(studentQueryService.getAllStudents()).thenReturn(List.of(student));

        mockMvc.perform(get("/api/v2/student/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Elena"));

    }

    @Test
    void getStudentByIdReturnsOk() throws Exception {
        String firstname="AAA";
        Long Id = 1L;
        StudentDetailResponse response = new StudentDetailResponse(
                Id,
                firstname,
                null,
                null,
                List.of(),
                List.of()
        );

        when(studentQueryService.getStudentById(Id)).thenReturn(response);

        mockMvc.perform(get("/api/v2/student/{id}/getStudentById", Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(Id))
                .andExpect(jsonPath("$.firstName").value("AAA"));

    }









}
