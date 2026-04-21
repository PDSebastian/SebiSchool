package ro.mycode.sebischool.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ro.mycode.sebischool.student.controller.StudentController;
import ro.mycode.sebischool.student.dtos.*;
import ro.mycode.sebischool.student.service.commandService.StudentCommandService;
import ro.mycode.sebischool.student.service.queryService.StudentQueryService;


import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentCommandService studentCommandService;

    @MockitoBean
    private StudentQueryService studentQueryService;

    @Test
    void testAddStudentReturnsOk() throws Exception {
        StudentRequest request = StudentRequest.builder()
                .firstName("Sebi")
                .lastName("Pop")
                .email("sebi@gmail.com")
                .build();

        StudentSummaryResponse response = StudentSummaryResponse.builder()
                .id(1L)
                .firstName("Sebi")
                .lastName("Pop")
                .build();

        when(studentCommandService.addStudent(request)).thenReturn(response);

        mockMvc.perform(post("/api/v2/student/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Sebi"));


    }
    @Test
    void testDeleteStudentReturnsOk() throws Exception {
        Long studentId = 1L;

        mockMvc.perform(delete("/api/v2/student/delete/" + studentId))
                .andExpect(status().isOk());


    }
    @Test
    void testPatchStudentReturnsOk() throws Exception {
        Long studentId = 1L;
        StudentPatchRequest patchRequest = StudentPatchRequest.builder()
                .firstName("Andrei")
                .build();

        StudentSummaryResponse response = StudentSummaryResponse.builder()
                .id(studentId)
                .firstName("Andrei")
                .build();

        when(studentCommandService.updatePatchStudent(studentId, patchRequest)).thenReturn(response);

        mockMvc.perform(patch("/api/v2/student/patch/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Andrei"));

    }
    @Test
    void testGetAllStudentsReturnsList() throws Exception {
        Long studentId = 1L;
        StudentSummaryResponse student = StudentSummaryResponse.builder().id(studentId).firstName("Ana").build();
        when(studentQueryService.getAllStudents()).thenReturn(List.of(student));

        mockMvc.perform(get("/api/v2/student/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Ana"));


    }
    @Test
    void testGetStudentByFirstName() throws Exception {
        String name = "Sebi";
        StudentSummaryResponse student = StudentSummaryResponse.builder().firstName(name).build();
        when(studentQueryService.getStudentsByFirstName(name)).thenReturn(List.of(student));

        mockMvc.perform(get("/api/v2/student/firstName/" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value(name));


    }
    @Test
    void testGetStudentDetailsReturnsOk() throws Exception {
        Long studentId = 1L;
        StudentDetailResponse response = StudentDetailResponse.builder()
                .Id(studentId)
                .email("test@gmail.com")
                .build();


        when(studentQueryService.getStudentById(studentId)).thenReturn(response);

        mockMvc.perform(get("/api/v2/student/" + studentId + "/getStudentById"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(studentId));

    }


}
