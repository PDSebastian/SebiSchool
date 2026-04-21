package ro.mycode.sebischool.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import ro.mycode.sebischool.books.controller.BookController;
import ro.mycode.sebischool.books.dtos.BookPatchRequest;
import ro.mycode.sebischool.books.service.commandService.BookCommandService;
import ro.mycode.sebischool.books.service.queryService.BookQueryService;
import ro.mycode.sebischool.books.dtos.BookResponse;
import ro.mycode.sebischool.books.dtos.Bookrequest;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(controllers = BookController.class)
public class bookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookCommandService bookCommandService;

    @MockitoBean
    private BookQueryService bookQueryService;

    @Test
    void addBookReturnsOk() throws Exception {
        Long studentId = 1L;
        Bookrequest request = new Bookrequest("Tttt", LocalDateTime.now());
        BookResponse response = new BookResponse(1L, "Tttt", LocalDateTime.now());
        when(bookCommandService.addBook(eq(studentId), any(Bookrequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v2/book/add/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookName").value("Tttt"));

    }
    @Test
    void deleteBookReturnsOk() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/v2/book/delete/{id}", id))
                .andExpect(status().isOk());

    }
    @Test
    void patchBookReturnsOk() throws Exception {
        Long id = 1L;
        BookPatchRequest patchRequest =BookPatchRequest.builder().bookName("Tttt").build();
        BookResponse response = new BookResponse(1L, "Tttt", LocalDateTime.now());

        when(bookCommandService.updatePatchBook(eq(id), any(BookPatchRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v2/book/patch/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value("Tttt"));

    }
    @Test
    void updateBookReturnsOk() throws Exception {
        Long id = 1L;
        Bookrequest request = new Bookrequest("assa", LocalDateTime.now());
        BookResponse response = new BookResponse(1L, "assa", LocalDateTime.now());

        when(bookCommandService.updateBook(eq(id), any(Bookrequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v2/book/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value("assa"));

        verify(bookCommandService).updateBook(eq(id), any(Bookrequest.class));
    }
    @Test
    void getAllBooksReturnsList() throws Exception {
        BookResponse book = new BookResponse(1L, "Tttt", LocalDateTime.now());
        when(bookQueryService.getAllBooks()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/v2/book/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].bookName").value("Tttt"));

    }
    @Test
    void getBookByBookNameReturnsItem() throws Exception {
        String bookName = "Tttt";
        BookResponse response = new BookResponse(1L, bookName, LocalDateTime.now());

        when(bookQueryService.getBookByBookName(bookName)).thenReturn(response);

        mockMvc.perform(get("/api/v2/book/bookName/{bookName}", bookName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value(bookName));

    }
    @Test
    void getBookByIdReturnsItem() throws Exception {
        Long id = 1L;
        BookResponse response = new BookResponse(id, "Tttt", LocalDateTime.now());

        when(bookQueryService.getBookById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v2/book/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.bookName").value("Tttt"));


    }


}
