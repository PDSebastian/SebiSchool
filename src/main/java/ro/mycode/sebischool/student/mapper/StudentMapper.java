package ro.mycode.sebischool.student.mapper;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.dtos.StudentRequest;
import ro.mycode.sebischool.student.dtos.StudentSummaryResponse;

@Component
public class StudentMapper {
    public Student toEntity(StudentRequest studentRequest) {
        if (studentRequest == null) {
            return null;
        }
        return Student.builder().firstName(studentRequest.getFirstName()).
                lastName(studentRequest.getLastName()).email(studentRequest.getEmail())
                .age(studentRequest.getAge()).build();


    }
    public StudentSummaryResponse toDto(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentSummaryResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getAge()

        );

    }




}
