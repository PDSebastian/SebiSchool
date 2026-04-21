package ro.mycode.sebischool.integration;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ro.mycode.sebischool.books.controller.BookController;
import ro.mycode.sebischool.books.dtos.BookPatchRequest;
import ro.mycode.sebischool.books.dtos.BookResponse;
import ro.mycode.sebischool.books.dtos.Bookrequest;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.repository.BookRepository;
import ro.mycode.sebischool.books.service.commandService.BookCommandService;
import ro.mycode.sebischool.books.service.queryService.BookQueryService;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = BookController.class)

public class BooksControllerTests {

    @Autowired
    private MockMvc mockMvc;

   @MockitoBean
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private StudentRepository studentRepository;
    @MockitoBean
    private BookCommandService  bookCommandService;

    @MockitoBean
    private BookQueryService bookQueryService;



    @BeforeEach
    public void cleanDataBase(){
//        bookRepository.deleteAll();
    }

    @Test
    void testAddBookReturnOk() throws Exception {
        Long studentId=10L;
        Bookrequest request = Bookrequest.builder()
                .bookName("Clean Code")
                .createdAt(LocalDateTime.now())
                .build();

        BookResponse response = new BookResponse(1L, "Clean Code", LocalDateTime.now());

        when(bookCommandService.addBook(studentId,request)).thenReturn(response);

        mockMvc.perform(post("/api/v2/book/add/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.bookName").value("Clean Code"));

    }
    @Test
    void updateBookReturnsUpdatedBook() throws Exception {
        Long bookId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Bookrequest updateRequest = Bookrequest.builder()
                .bookName("LLLLLL")
                .createdAt(now)
                .build();

        BookResponse response = new BookResponse(bookId, "LLLLLL", now);

        when(bookCommandService.updateBook(bookId, updateRequest)).thenReturn(response);

        mockMvc.perform(put("/api/v2/book/update/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value("LLLLLL"));



    }
    @Test
    void patchBookReturnsOk() throws Exception {
        Long bookId = 1L;
        BookPatchRequest patchRequest = BookPatchRequest.builder()
                .bookName("AAAA")
                .build();

        BookResponse response = new BookResponse(bookId, "AAAA", LocalDateTime.now());

        when(bookCommandService.updatePatchBook(bookId, patchRequest)).thenReturn(response);

        mockMvc.perform(patch("/api/v2/book/patch/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value("AAAA"));

    }
    @Test
    void deleteBookReturnsOk() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/api/v2/book/delete/" + bookId))
                .andExpect(status().isOk());

    }
    @Test
    void getBookByIdReturnsBook() throws Exception {
        Long bookId = 1L;
        BookResponse response = new BookResponse(bookId, "AAAAA", LocalDateTime.now());

        when(bookQueryService.getBookById(bookId)).thenReturn(response);

        mockMvc.perform(get("/api/v2/book/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.bookName").value("AAAAA"));

        verify(bookQueryService).getBookById(bookId);
    }
    @Test
    void getAllBooksReturnsList() throws Exception {
        BookResponse book = new BookResponse(1L, "Java Basics", LocalDateTime.now());
        when(bookQueryService.getAllBooks()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/v2/book/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].bookName").value("Java Basics"));


    }
    @Test
    void getBookByBookNameReturnsBook() throws Exception {
        String name = "dwdwdwd";
        BookResponse response = new BookResponse(1L, name, LocalDateTime.now());

        when(bookQueryService.getBookByBookName(name)).thenReturn(response);

        mockMvc.perform(get("/api/v2/book/bookName/" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value(name));


    }




}
