package ro.mycode.sebischool.professor.dtos;

import ro.mycode.sebischool.course.model.Course;

import java.util.Set;

public record ProfessorSummaryResponse(
        Long id,
        String firstName,
        String lastName,
        String specialty,
        String email,
        String departament,
        int yearExperience,
        Set<Course>courses




) {
}
