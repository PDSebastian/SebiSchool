package ro.mycode.sebischool.student.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.mycode.sebischool.student.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    @EntityGraph(attributePaths = "books")
    List<Student> findAll();
    List<Student> findAllByEnrolments_Course_Name(String courseName);
    List<Student> findAllByBooksBookName(String bookName);


    Optional<Student> findByEmail( String email);
    List<Student> findStudentByFirstName(String firstName);

    @EntityGraph(attributePaths = {"enrolments", "enrolments.course", "books"})
    @Query("SELECT s FROM Student s WHERE s.id = :studentID")
    Optional<Student> findStudentById(@Param("studentID") Long studentID);
}
