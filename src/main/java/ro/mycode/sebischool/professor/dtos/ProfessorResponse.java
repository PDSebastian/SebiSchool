package ro.mycode.sebischool.professor.dtos;

import lombok.Builder;

@Builder
public record ProfessorResponse(
        Long id,
        String firstName,
        String lastName,
        String specialty,
        String departament,
        int yearExperience


        ) {
}
