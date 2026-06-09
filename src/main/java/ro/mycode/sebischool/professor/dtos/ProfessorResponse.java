package ro.mycode.sebischool.professor.dtos;

import lombok.Builder;
import ro.mycode.sebischool.course.model.Course;

import java.util.Set;

@Builder
public record ProfessorResponse(
        Long id,
        String firstName,
        String lastName,
        String email, String specialty, String departament, int yearExperience,
        Set<Course> courses


        ) {
}
