package ro.mycode.sebischool.student.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.mycode.sebischool.student.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @EntityGraph(attributePaths = "books")
    List<Student> findAll();

    List<Student> findAllByEnrolments_Course_Name(String courseName);

    List<Student> findAllByBooksBookName(String bookName);

    // Identitatea (email, firstName, lastName) traieste pe User — query-urile
    // se fac prin relatia user.
    Optional<Student> findByUserId(Long userId);

    Optional<Student> findByUserEmail(String email);

    List<Student> findByUserFirstName(String firstName);

    boolean existsByUserEmail(String email);

    @EntityGraph(attributePaths = {"enrolments", "enrolments.course", "books"})
    @Query("SELECT s FROM Student s WHERE s.id = :studentID")
    Optional<Student> findStudentById(@Param("studentID") Long studentID);
}
