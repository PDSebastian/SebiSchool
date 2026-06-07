package ro.mycode.sebischool.professor.dtos;

import jakarta.validation.constraints.NotBlank;

public record ProfessorPatchRequest(

       @NotBlank(message = "Specializarea nu poate fi lasata goala")
        String specialty

) {
}
