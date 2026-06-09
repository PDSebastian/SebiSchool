package ro.mycode.sebischool.professor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.mycode.sebischool.enrolment.service.queryService.EnrolmentQueryService;
import ro.mycode.sebischool.professor.dtos.ProfessorPatchRequest;
import ro.mycode.sebischool.professor.dtos.ProfessorRequest;
import ro.mycode.sebischool.professor.dtos.ProfessorResponse;
import ro.mycode.sebischool.professor.service.ProfessorCommandService;
import ro.mycode.sebischool.professor.service.ProfessorQueryService;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v2/profesori")
@Slf4j
public class ProfessorController {
    private final EnrolmentQueryService enrolmentQueryService;
    private final ProfessorQueryService professorQueryService;
    private final ProfessorCommandService professorCommandService;

    public ProfessorController(ProfessorCommandService professorCommandService,
                               ProfessorQueryService professorQueryService,
                               EnrolmentQueryService enrolmentQueryService) {
        this.professorCommandService = professorCommandService;
        this.professorQueryService = professorQueryService;
        this.enrolmentQueryService = enrolmentQueryService;
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        professorCommandService.deleteProfessor(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public ResponseEntity<ProfessorResponse> updateProfessor(@PathVariable Long id, @RequestBody ProfessorRequest professorRequest) {
        ProfessorResponse professorResponse = professorCommandService.updateProfessor(id, professorRequest);
        return ResponseEntity.status(HttpStatus.OK).body(professorResponse);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<ProfessorResponse> updatePatch(@PathVariable Long id , @RequestBody ProfessorPatchRequest professorPatchRequest) {
        ProfessorResponse professorResponse = professorCommandService.patchProfessor(id,  professorPatchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(professorResponse);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public ResponseEntity<List<ProfessorResponse>> getAllProfessors() {
        List<ProfessorResponse> professorResponses = professorQueryService.getAllProfessors();
        return ResponseEntity.status(HttpStatus.OK).body(professorResponses);
    }

    @PostMapping("/{id}/courses/{courseId}")
    @PreAuthorize("hasAuthority('COURSE_MANAGE')")
    public ResponseEntity<ProfessorResponse> assignCourse(@PathVariable Long id, @PathVariable Long courseId) {
        ProfessorResponse professorResponse = professorCommandService.assignCourse(id, courseId);
        return ResponseEntity.status(HttpStatus.OK).body(professorResponse);
    }

    @DeleteMapping("/{id}/courses/{courseId}")
    @PreAuthorize("hasAuthority('COURSE_MANAGE')")
    public ResponseEntity<ProfessorResponse> unassignCourse(@PathVariable Long id, @PathVariable Long courseId) {
        ProfessorResponse professorResponse = professorCommandService.unassignCourse(id, courseId);
        return ResponseEntity.status(HttpStatus.OK).body(professorResponse);
    }

    @GetMapping("/{professorId}/students")
    @PreAuthorize("hasAuthority('STUDENT_VIEW')")
    public ResponseEntity<List<StudentSummaryResponse>> getStudentsOfProfessor(@PathVariable Long professorId) {
        List<StudentSummaryResponse> students = enrolmentQueryService.getStudentsByProfessorId(professorId);
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }
}
