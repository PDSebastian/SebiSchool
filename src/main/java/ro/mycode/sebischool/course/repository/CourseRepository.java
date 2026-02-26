package ro.mycode.sebischool.course.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.enrolment.model.Enrolment;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course>findAll();
    List<Course>findAllByEnrolmentsIsNotEmpty();
    Optional<Course> findById(long id);


    @EntityGraph(attributePaths = "enrolments")


    Optional<Course> findByName(String name);
    List<Course> findAllByEnrolmentsStudentEmail(String email);
    List<Course> id(Long id);
}
