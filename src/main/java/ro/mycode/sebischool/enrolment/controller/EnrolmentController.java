package ro.mycode.sebischool.enrolment.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.mycode.sebischool.enrolment.service.commandService.EnrolmentCommandService;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentPatchRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;
import ro.mycode.sebischool.enrolment.service.queryService.EnrolmentQueryService;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v2/enrolments")
@Slf4j
public class EnrolmentController {
    private EnrolmentCommandService enrolmentCommandService;
    private EnrolmentQueryService enrolmentQueryService;
    public EnrolmentController(EnrolmentCommandService enrolmentCommandService, EnrolmentQueryService enrolmentQueryService) {
        this.enrolmentCommandService = enrolmentCommandService;
        this.enrolmentQueryService = enrolmentQueryService;

    }
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ENROL_SELF')")
    public ResponseEntity<EnrolmentResponse> addEnrolment(@Valid @RequestBody EnrolmentRequest enrolmentRequest) {
        log.debug("http post /api/v2/enrolments");
        EnrolmentResponse response = enrolmentCommandService.addEnrolment(enrolmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<Void> deleteEnrolment(@PathVariable Long id) {
        log.debug("http delete /api/v2/enrolments/{}", id);
       enrolmentCommandService.deleteEnrolment(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<EnrolmentResponse> patchEnrolment(@PathVariable Long id, @RequestBody EnrolmentPatchRequest request) {
        log.debug("http patch /api/v2/enrolments/{}", id);
        EnrolmentResponse response = enrolmentCommandService.patchEnrolment(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EnrolmentResponse> updateEnrolment(@PathVariable Long id, @Valid @RequestBody EnrolmentRequest request) {
        log.debug("http put /api/v2/enrolments/{}", id);
        EnrolmentResponse response = enrolmentCommandService.updateEnrolment(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("")
    public ResponseEntity<List<EnrolmentResponse>> getAllEnrolments() {
        log.debug("http get /api/v2/enrolments");
        List<EnrolmentResponse> response = enrolmentQueryService.getAllEnrolments();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrolmentResponse>> getEnrolmentsByStudent(@PathVariable Long courseId) {
        log.debug("http get /api/v2/enrolments/course/{}", courseId);
        List<EnrolmentResponse> response = enrolmentQueryService.getAllEnrolmentsByCourseId(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/course/{courseId}/students")
    public ResponseEntity< List<StudentSummaryResponse> >getStudentsByCourseId(@PathVariable Long courseId) {
        List<StudentSummaryResponse> response = enrolmentQueryService.getStudentsByCourseId(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }




}
