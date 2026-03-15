package ro.mycode.sebischool.enrolment.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mycode.sebischool.enrolment.service.commanService.EnrolmnetCommandService;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentPatchRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentRequest;
import ro.mycode.sebischool.enrolment.dtos.EnrolmentResponse;
import ro.mycode.sebischool.enrolment.service.queryService.EnrolmentQueryService;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v2/enrolment")
@Slf4j
public class EnrolmentController {
    private EnrolmnetCommandService  enrolmnetCommandService;
    private EnrolmentQueryService enrolmentQueryService;
    public EnrolmentController(EnrolmnetCommandService enrolmnetCommandService, EnrolmentQueryService enrolmentQueryService) {
        this.enrolmnetCommandService = enrolmnetCommandService;
        this.enrolmentQueryService = enrolmentQueryService;

    }
    @PostMapping("/add")
    public ResponseEntity<EnrolmentResponse> addEnrolment(@Valid @RequestBody EnrolmentRequest enrolmentRequest) {
        log.debug("http post /api/v2/enrolment/add");
        EnrolmentResponse response = enrolmnetCommandService.addEnrolment(enrolmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<EnrolmentResponse> deleteEnrolment(@PathVariable Long id) {
        log.debug("http delete /api/v2/enrolment/delete/{}", id);
        EnrolmentResponse response = enrolmnetCommandService.deleteEnrolment(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PatchMapping("/patch/{id}")
    public ResponseEntity<EnrolmentResponse> patchEnrolment(@PathVariable Long id, @RequestBody EnrolmentPatchRequest request) {
        log.debug("http patch /api/v2/enrolment/patch/{}", id);
        EnrolmentResponse response = enrolmnetCommandService.patchEnrolment(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<EnrolmentResponse> updateEnrolment(@PathVariable Long id, @Valid @RequestBody EnrolmentRequest request) {
        log.debug("http put /api/v2/enrolment/update/{}", id);
        EnrolmentResponse response = enrolmnetCommandService.updateEnrolment(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/all")
    public ResponseEntity<List<EnrolmentResponse>> getAllEnrolments() {
        log.debug("http get /api/v2/enrolment/all");
        List<EnrolmentResponse> response = enrolmentQueryService.getAllEnrolments();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/course/{courseID}")
    public ResponseEntity<List<EnrolmentResponse>> getEnrolmentsByStudent(@PathVariable Long courseID) {
        log.debug("http get /api/v2/enrolment/student/{}", courseID);
        List<EnrolmentResponse> response = enrolmentQueryService.getAllEnrolmentsByCourseId(courseID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("course/{courseID}/students")
    public ResponseEntity< List<StudentSummaryResponse> >getStudentsByCourseId(@PathVariable Long courseID) {
        List<StudentSummaryResponse> response = enrolmentQueryService.getStudentsByCourseId(courseID);
        return ResponseEntity.status(HttpStatus.OK).body(response);


    }




}
