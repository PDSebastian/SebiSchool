package ro.mycode.sebischool.course.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mycode.sebischool.course.service.commandService.CourseCommandService;
import ro.mycode.sebischool.course.dtos.CoursePatchRequest;
import ro.mycode.sebischool.course.dtos.CourseRequest;
import ro.mycode.sebischool.course.dtos.CourseResponse;
import ro.mycode.sebischool.course.service.queryService.CourseQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/course/")
@Slf4j
public class CourseController {
    private CourseCommandService courseCommandService;
    private CourseQueryService courseQueryService;
    public CourseController(CourseCommandService courseCommandService, CourseQueryService courseQueryService) {
        this.courseCommandService = courseCommandService;
        this.courseQueryService = courseQueryService;

    }
    @PostMapping("/add")
    public ResponseEntity<CourseResponse> addCourse(@Valid @RequestBody CourseRequest courseRequest) {
        log.debug("http post /api/v2/courses/add");
        CourseResponse c = courseCommandService.addCourse(courseRequest);
        return ResponseEntity.status(HttpStatus.OK).body(c);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CourseResponse> deleteCourse(@PathVariable Long id) {
        log.debug("http post /api/v2/courses/delete/{id}");
        CourseResponse c=courseCommandService.deleteCourse(id);
        return ResponseEntity.status(HttpStatus.OK).body(c);

    }
    @PatchMapping("/patch/{id}")
    public ResponseEntity<CourseResponse> patchCourse(@PathVariable Long id, @RequestBody CoursePatchRequest coursePatchRequest) {
        log.debug("http patch /api/v2/course/patch/{}", id);
        CourseResponse c = courseCommandService.updatePatchCourse(id, coursePatchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(c);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest courseRequest) {
        log.debug("http put /api/v2/course/update/{}", id);
        CourseResponse c = courseCommandService.updateCourse(id, courseRequest);
        return ResponseEntity.status(HttpStatus.OK).body(c);
    }
    @GetMapping("/all")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        log.debug("http get /api/v2/course/all");
        List<CourseResponse> courses = courseQueryService.getAllCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<CourseResponse> getCourseByName(@PathVariable String name) {
        log.debug("http get /api/v2/course/name/{}", name);
        CourseResponse course = courseQueryService.getCourseByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(course);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        log.debug("http get /api/v2/course/{}", id);
        CourseResponse r=courseQueryService.getCourseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(r);

    }





}
