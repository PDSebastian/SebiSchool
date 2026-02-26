package ro.mycode.sebischool.student.service.mapper;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.service.dtos.StudentRequest;
import ro.mycode.sebischool.student.service.dtos.StudentResponse;

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
    public StudentResponse toDto(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getAge()

        );
    }




}
