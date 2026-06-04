package ro.mycode.sebischool.professor.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ProfessorRequest(
        @NotBlank (message = "Numele nu poate fi gol")
        String firstName,
        @NotBlank(message = "Prenumele nu poate fi gol")
        String lastName,
        @NotBlank(message = "Speciallizare nu poate fi goala")
        String specialty,
        @NotBlank(message = "Departamentul este obligatoriu")
        String departament,

        int yearExperience




) {
}
