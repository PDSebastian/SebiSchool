package ro.mycode.sebischool.course.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.mycode.sebischool.course.service.commandService.CourseCommandService;
import ro.mycode.sebischool.course.dtos.CoursePatchRequest;
import ro.mycode.sebischool.course.dtos.CourseRequest;
import ro.mycode.sebischool.course.dtos.CourseSummaryResponse;
import ro.mycode.sebischool.course.service.queryService.CourseQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/courses")
@Slf4j
public class    CourseController {
    private CourseCommandService courseCommandService;
    private CourseQueryService courseQueryService;
    public CourseController(CourseCommandService courseCommandService, CourseQueryService courseQueryService) {
        this.courseCommandService = courseCommandService;
        this.courseQueryService = courseQueryService;

    }
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('COURSE_MANAGE')")
    public ResponseEntity<CourseSummaryResponse> addCourse(@Valid @RequestBody CourseRequest courseRequest) {
        log.debug("http post /api/v2/courses");
        CourseSummaryResponse c = courseCommandService.addCourse(courseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COURSE_MANAGE')")
    public ResponseEntity<CourseSummaryResponse> deleteCourse(@PathVariable Long id) {
        log.debug("http delete /api/v2/courses/{}", id);
        CourseSummaryResponse c=courseCommandService.deleteCourse(id);
        return ResponseEntity.status(HttpStatus.OK).body(c);

    }
    @PatchMapping("/{id}")
    public ResponseEntity<CourseSummaryResponse> patchCourse(@PathVariable Long id, @RequestBody CoursePatchRequest coursePatchRequest) {
        log.debug("http patch /api/v2/courses/{}", id);
        CourseSummaryResponse c = courseCommandService.updatePatchCourse(id, coursePatchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(c);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COURSE_MANAGE')")
    public ResponseEntity<CourseSummaryResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest courseRequest) {
        log.debug("http put /api/v2/courses/{}", id);
        CourseSummaryResponse c = courseCommandService.updateCourse(id, courseRequest);
        return ResponseEntity.status(HttpStatus.OK).body(c);
    }
    @GetMapping("")
    @PreAuthorize("hasAuthority('COURSE_VIEW')")
    public ResponseEntity<List<CourseSummaryResponse>> getAllCourses() {
        log.debug("http get /api/v2/courses");
        List<CourseSummaryResponse> courses = courseQueryService.getAllCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }
    @GetMapping("/by-name/{name}")
    public ResponseEntity<CourseSummaryResponse> getCourseByName(@PathVariable String name) {
        log.debug("http get /api/v2/courses/by-name/{}", name);
        CourseSummaryResponse course = courseQueryService.getCourseByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(course);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CourseSummaryResponse> getCourseById(@PathVariable Long id) {
        log.debug("http get /api/v2/courses/{}", id);
        CourseSummaryResponse r=courseQueryService.getCourseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(r);

    }





}
