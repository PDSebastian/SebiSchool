package ro.mycode.sebischool.student.service.queryService;

import org.springframework.data.jpa.repository.EntityGraph;
import ro.mycode.sebischool.student.service.dtos.StudentResponse;

import java.util.List;
import java.util.Optional;

public interface StudentQueryService {
    List<StudentResponse> getAllStudents();
    Optional<StudentResponse> getStudentByEmail(String email);
    Optional<StudentResponse> getStudentByFirstNameAndLastName(String firstName,String lastName);

    List<StudentResponse> getStudentsByFirstName(String firstName);
}
