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
import ro.mycode.sebischool.professor.dtos.ProfessorRequest;
import ro.mycode.sebischool.professor.model.Professor;
import ro.mycode.sebischool.professor.repository.ProfessorRepository;
import ro.mycode.sebischool.users.model.User;
import ro.mycode.sebischool.users.model.UserType;
import ro.mycode.sebischool.users.repository.UserRepository;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * INTEGRATION TEST pentru Professor — un profesor este legat 1-la-1 de un User,
 * deci seedam User(PROFESSOR) + Professor. Endpoint-urile cer USER_EDIT,
 * asa ca punem @WithMockUser cu aceasta autoritate.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class ProfessorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    private Long professorId;

    @BeforeEach
    void seedDatabase() {
        User user = User.builder()
                .firstName("Simona")
                .lastName("Radu")
                .email("simona.radu@scoala.ro")
                .password("secret")
                .userType(UserType.PROFESSOR)
                .build();
        User savedUser = userRepository.save(user);

        Professor professor = Professor.builder()
                .specialty("Java")
                .departament("IT")
                .yearExperience(5)
                .user(savedUser)
                .courses(new HashSet<>())
                .build();
        professorId = professorRepository.save(professor).getId();
    }

    @Test
    @WithMockUser(authorities = "USER_EDIT")
    @DisplayName("GET /api/v2/profesori intoarce profesorul seedat")
    void getAllProfessors_returnsSeeded() throws Exception {
        mockMvc.perform(get("/api/v2/profesori"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Simona"))
                .andExpect(jsonPath("$[0].specialty").value("Java"));
    }

    @Test
    @WithMockUser(authorities = "USER_EDIT")
    @DisplayName("PUT /api/v2/profesori/{id} persista specializarea si experienta")
    void updateProfessor_persistsChange() throws Exception {
        ProfessorRequest request = ProfessorRequest.builder()
                .firstName("Simona")
                .lastName("Radu")
                .specialty("Spring")
                .departament("CS")
                .yearExperience(8)
                .build();

        mockMvc.perform(put("/api/v2/profesori/" + professorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialty").value("Spring"))
                .andExpect(jsonPath("$.yearExperience").value(8));

        Professor updated = professorRepository.findById(professorId).orElseThrow();
        assertEquals("Spring", updated.getSpecialty());
        assertEquals(8, updated.getYearExperience());
    }

    @Test
    @WithMockUser(authorities = "USER_EDIT")
    @DisplayName("PUT /api/v2/profesori/{id} inexistent intoarce 404")
    void updateProfessor_notFound_returns404() throws Exception {
        ProfessorRequest request = ProfessorRequest.builder()
                .firstName("X")
                .lastName("Y")
                .specialty("Spring")
                .departament("CS")
                .yearExperience(8)
                .build();

        mockMvc.perform(put("/api/v2/profesori/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
