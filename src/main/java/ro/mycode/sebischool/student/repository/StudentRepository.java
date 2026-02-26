package ro.mycode.sebischool.student.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.mycode.sebischool.student.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    @EntityGraph(attributePaths = "books")
    List<Student> findAll();
    List<Student> findAllByEnrolments_Course_Name(String courseName);
    List<Student> findAllByBooksBookName(String bookName);






}
