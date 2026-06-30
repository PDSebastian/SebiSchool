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
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.enrolment.repository.EnrolmentRepository;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.users.model.User;
import ro.mycode.sebischool.users.model.UserType;
import ro.mycode.sebischool.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * INTEGRATION TEST pentru Enrolment — o inscriere leaga un Student de un Course,
 * deci seedam User -> Student -> Course -> Enrolment.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class EnrolmentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrolmentRepository enrolmentRepository;

    private Long studentId;
    private Long courseId;
    private Long enrolmentId;

    @BeforeEach
    void seedDatabase() {
        User user = User.builder()
                .firstName("Dan")
                .lastName("Marin")
                .email("dan@scoala.ro")
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

        Course course = Course.builder()
                .name("Java")
                .departament("IT")
                .build();
        courseId = courseRepository.save(course).getId();

        Enrolment enrolment = Enrolment.builder()
                .student(student)
                .course(course)
                .createdAt(LocalDateTime.of(2024, 1, 1, 9, 0))
                .build();
        enrolmentId = enrolmentRepository.save(enrolment).getId();
    }

    @Test
    @DisplayName("GET /api/v2/enrolments intoarce inscrierea seedata")
    void getAllEnrolments_returnsSeeded() throws Exception {
        mockMvc.perform(get("/api/v2/enrolments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(enrolmentId));
    }

    @Test
    @DisplayName("GET /api/v2/enrolments/course/{courseId} intoarce inscrierile cursului")
    void getEnrolmentsByCourse_returnsList() throws Exception {
        mockMvc.perform(get("/api/v2/enrolments/course/" + courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(authorities = "ENROL_SELF")
    @DisplayName("POST /api/v2/enrolments/add creeaza o inscriere noua intr-un alt curs")
    void addEnrolment_persistsNew() throws Exception {
        // alt curs, ca sa nu lovim regula 'inscriere deja existenta'
        Course otherCourse = Course.builder().name("Spring").departament("IT").build();
        Long otherCourseId = courseRepository.save(otherCourse).getId();

        EnrolmentRequest request = EnrolmentRequest.builder()
                .studentId(studentId)
                .courseId(otherCourseId)
                .build();

        mockMvc.perform(post("/api/v2/enrolments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        assertTrue(enrolmentRepository.existsByStudentIdAndCourseId(studentId, otherCourseId));
    }

    @Test
    @WithMockUser(authorities = "USER_DELETE")
    @DisplayName("DELETE /api/v2/enrolments/{id} sterge inscrierea din DB")
    void deleteEnrolment_removesFromDatabase() throws Exception {
        mockMvc.perform(delete("/api/v2/enrolments/" + enrolmentId))
                .andExpect(status().isNoContent());

        assertTrue(enrolmentRepository.findById(enrolmentId).isEmpty());
    }
}
