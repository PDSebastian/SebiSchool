package ro.mycode.sebischool.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.student.dtos.StudentRequest;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.users.model.User;
import ro.mycode.sebischool.users.model.UserType;
import ro.mycode.sebischool.users.repository.UserRepository;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * INTEGRATION TEST — testeaza tot lantul real: HTTP -> Controller -> Service -> Repository -> DB.
 *
 * Diferenta fata de unit test:
 *  - @SpringBootTest porneste CONTEXTUL Spring complet (toate bean-urile reale)
 *  - DB-ul e REAL (H2 in memorie, vezi application-test.yaml), nu mock
 *  - cererile trec prin MockMvc exact ca o cerere HTTP adevarata
 *
 * Detalii importante:
 *  - @ActiveProfiles("test") -> foloseste H2 in loc de MySQL-ul din application.yaml
 *  - @AutoConfigureMockMvc(addFilters = false) -> dezactiveaza filtrul de securitate,
 *    ca sa testam logica de date fara token JWT (securitatea o testam separat)
 *  - @Transactional -> dupa fiecare test se face ROLLBACK, deci DB-ul ramane curat
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class StudentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Long studentId;

    @BeforeEach
    void seedDatabase() {
        // Identitatea traieste pe User; Student tine doar campurile de rol (age).
        // User-ul nu are cascada de pe Student, deci il salvam INTAI.
        User user = User.builder()
                .firstName("Maria")
                .lastName("Pop")
                .email("maria.pop@scoala.ro")
                .password("secret")
                .userType(UserType.STUDENT)
                .build();
        User savedUser = userRepository.save(user);

        Student student = Student.builder()
                .age(20)
                .user(savedUser)
                .books(new HashSet<>())
                .enrolments(new HashSet<>())
                .build();
        studentId = studentRepository.save(student).getId();
    }

    @Test
    @DisplayName("GET /api/v2/students intoarce studentul seedat din DB")
    void getAllStudents_returnsSeededStudent() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/api/v2/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Maria"))
                .andExpect(jsonPath("$[0].age").value(20));
    }

    @Test
    @DisplayName("GET /api/v2/students/{id} intoarce detaliile studentului")
    void getStudentById_returnsDetails() throws Exception {
        // Act + Assert (atentie: cheia JSON e 'Id' cu I mare, asa e definit DTO-ul)
        mockMvc.perform(get("/api/v2/students/" + studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(studentId))
                .andExpect(jsonPath("$.firstName").value("Maria"))
                .andExpect(jsonPath("$.email").value("maria.pop@scoala.ro"));
    }

    @Test
    @WithMockUser(authorities = "USER_EDIT")
    @DisplayName("PUT /api/v2/students/{id} chiar persista modificarea in DB")
    void updateStudent_persistsChange() throws Exception {
        // Arrange
        StudentRequest request = StudentRequest.builder()
                .firstName("Maria")
                .lastName("Popescu")
                .email("maria.popescu@scoala.ro")
                .age(25)
                .build();

        // Act
        mockMvc.perform(put("/api/v2/students/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Popescu"))
                .andExpect(jsonPath("$.age").value(25));

        // Assert — citim direct din repository ca sa dovedim ca s-a salvat in DB
        Student updated = studentRepository.findById(studentId).orElseThrow();
        assertEquals(25, updated.getAge());
        assertEquals("Popescu", updated.getUser().getLastName());
    }

    @Test
    @WithMockUser(authorities = "USER_DELETE")
    @DisplayName("DELETE /api/v2/students/{id} sterge studentul din DB")
    void deleteStudent_removesFromDatabase() throws Exception {
        // Act
        mockMvc.perform(delete("/api/v2/students/" + studentId))
                .andExpect(status().isNoContent());

        // Assert — studentul nu mai exista in DB
        assertTrue(studentRepository.findById(studentId).isEmpty());
    }

    @Test
    @DisplayName("GET /api/v2/students/{id} inexistent intoarce 404")
    void getStudentById_notFound_returns404() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/api/v2/students/999999"))
                .andExpect(status().isNotFound());
    }
}
