package ro.mycode.sebischool.books.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mycode.sebischool.books.service.commandService.BookCommandService;
import ro.mycode.sebischool.books.dtos.BookPatchRequest;
import ro.mycode.sebischool.books.dtos.BookResponse;
import ro.mycode.sebischool.books.dtos.Bookrequest;
import ro.mycode.sebischool.books.service.queryService.BookQueryService;

import java.util.List;


@RestController
@RequestMapping("/api/v2/book")
@Slf4j
public class BookController {
    private BookCommandService bookCommandService;
    private BookQueryService bookQueryService;
    public BookController(BookCommandService bookCommandService, BookQueryService bookQueryService) {
        this.bookCommandService = bookCommandService;
        this.bookQueryService = bookQueryService;
    }

    @PostMapping("/add/{studentId}")
    public ResponseEntity<BookResponse> addBook(@PathVariable Long studentId, @Valid @RequestBody Bookrequest bookRequest) {
        log.debug("http post /api/v2/books/add/{}", studentId);
        BookResponse b = bookCommandService.addBook(studentId, bookRequest);
        return ResponseEntity.status(HttpStatus.OK).body(b);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("http delete /api/v2/books/delete/{}", id);
        bookCommandService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<BookResponse> patchBook(@PathVariable Long id, @Valid @RequestBody BookPatchRequest bookPatchRequest) {
        log.debug("http patch /api/v2/books/patch/{}", id);
        BookResponse b = bookCommandService.updatePatchBook(id, bookPatchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(b);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody Bookrequest bookRequest) {
        log.debug("http put /api/v2/books/update/{}", id);
        BookResponse b = bookCommandService.updateBook(id, bookRequest);
        return ResponseEntity.status(HttpStatus.OK).body(b);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        log.debug("http get /api/v2/books/all");
        List<BookResponse> b = bookQueryService.getAllBooks();
        return ResponseEntity.status(HttpStatus.OK).body(b);
    }

    @GetMapping("/bookName/{bookName}")
    public ResponseEntity<BookResponse> getBookByBookName(@PathVariable String bookName) {
        log.debug("http get /api/v2/books/bookName/{}", bookName);
        BookResponse b = bookQueryService.getBookByBookName(bookName);
        return ResponseEntity.status(HttpStatus.OK).body(b);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        log.debug("http get /api/v2/books/{}", id);
       BookResponse b=bookQueryService.getBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(b);
    }











}
