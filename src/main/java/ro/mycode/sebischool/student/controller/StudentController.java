package ro.mycode.sebischool.student.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;
import ro.mycode.sebischool.student.dtos.*;
import ro.mycode.sebischool.student.service.commandService.StudentCommandService;
import ro.mycode.sebischool.student.service.queryService.StudentQueryService;

import java.util.List;


@RestController
@RequestMapping("/api/v2/student")
@Slf4j
public class StudentController {
    private StudentCommandService studentCommandService;
    private StudentQueryService studentQueryService;
    public StudentController(StudentCommandService studentCommandService, StudentQueryService studentQueryService) {
        this.studentCommandService = studentCommandService;
        this.studentQueryService = studentQueryService;
    }
    @PostMapping("/add")
    public ResponseEntity<StudentSummaryResponse> addStudent(@Valid @RequestBody StudentRequest studentRequest) {
        log.debug("http post /api/v2/students/add");
        StudentSummaryResponse s = studentCommandService.addStudent(studentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(s);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        log.debug("http delete /api/v2/student/delete/{}", id);
        studentCommandService.deleteStudent(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PatchMapping("/patch/{id}")
    public ResponseEntity<StudentSummaryResponse> patchStudent(@PathVariable Long id, @Valid @RequestBody StudentPatchRequest studentPatchRequest) {
        log.debug("http patch /api/v2/student/patch/{}", id);
        StudentSummaryResponse s = studentCommandService.updatePatchStudent(id, studentPatchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(s);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<StudentSummaryResponse> updateStudent(@PathVariable String email, @Valid @RequestBody StudentRequest studentRequest) {
        log.debug("http put /api/v2/student/update/{}", email);
        StudentSummaryResponse s = studentCommandService.updateStudent(studentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(s);
    }
    @GetMapping("/all")
    public ResponseEntity<List<StudentSummaryResponse>> getAllStudents() {
        log.debug("http get /api/v2/student/all");
        List<StudentSummaryResponse> students = studentQueryService.getAllStudents();
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @GetMapping("/firstName/{firstName}")
    public ResponseEntity<List<StudentSummaryResponse>> getStudentByFirstName(@PathVariable String firstName) {
        log.debug("http get /api/v2/student/firstName/{}", firstName);
        List<StudentSummaryResponse> students = studentQueryService.getStudentsByFirstName(firstName);
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }
    @GetMapping("/{id}/getStudentById")
    public ResponseEntity<StudentDetailResponse> getStudentDetails(@PathVariable Long id) {
        log.debug("http get /api/v2/student/details/{}", id);
        StudentDetailResponse s= studentQueryService.getStudentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(s);
    }





}
