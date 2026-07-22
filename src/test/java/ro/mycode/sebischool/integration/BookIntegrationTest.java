package ro.mycode.sebischool.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ro.mycode.sebischool.books.dtos.Bookrequest;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.repository.BookRepository;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;
import ro.mycode.sebischool.users.model.User;
import ro.mycode.sebischool.users.model.UserType;
import ro.mycode.sebischool.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * INTEGRATION TEST pentru Book — o carte apartine unui Student, deci seedam
 * intai User -> Student -> Book. Endpoint-urile de carti nu au @PreAuthorize,
 * iar securitatea e oprita (addFilters=false), deci nu avem nevoie de @WithMockUser.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BookRepository bookRepository;

    private Long studentId;
    private Long bookId;

    @BeforeEach
    void seedDatabase() {
        User user = User.builder()
                .firstName("Ion")
                .lastName("Ionescu")
                .email("ion@scoala.ro")
                .password("secret")
                .userType(UserType.STUDENT)
                .build();
        User savedUser = userRepository.save(user);

        Student student = Student.builder()
                .age(21)
                .user(savedUser)
                .books(new HashSet<>())
                .enrolments(new HashSet<>())
                .build();
        studentId = studentRepository.save(student).getId();

        Book book = Book.builder()
                .bookName("Clean Code")
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .student(student)
                .build();
        bookId = bookRepository.save(book).getId();
    }

    @Test
    @DisplayName("GET /api/v2/books intoarce cartea seedata")
    void getAllBooks_returnsSeededBook() throws Exception {
        mockMvc.perform(get("/api/v2/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].bookName").value("Clean Code"));
    }

    @Test
    @DisplayName("GET /api/v2/books/{id} intoarce numele cartii")
    void getBookById_returnsBook() throws Exception {
        mockMvc.perform(get("/api/v2/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value("Clean Code"));
    }

    @Test
    @DisplayName("POST /api/v2/books/student/{studentId} adauga o carte in DB")
    void addBook_persistsNewBook() throws Exception {
        Bookrequest request = Bookrequest.builder()
                .bookName("Effective Java")
                .createdAt(LocalDateTime.of(2024, 2, 2, 12, 0))
                .build();

        mockMvc.perform(post("/api/v2/books/student/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookName").value("Effective Java"));

        assertEquals(2, bookRepository.findAll().size());
    }

    @Test
    @DisplayName("DELETE /api/v2/books/{id} sterge cartea din DB")
    void deleteBook_removesFromDatabase() throws Exception {
        mockMvc.perform(delete("/api/v2/books/" + bookId))
                .andExpect(status().isNoContent());

        assertTrue(bookRepository.findById(bookId).isEmpty());
    }

    @Test
    @DisplayName("GET /api/v2/books/{id} inexistent intoarce 404")
    void getBookById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/v2/books/999999"))
                .andExpect(status().isNotFound());
    }
}
