package ro.mycode.sebischool.student.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record StudentPatchRequest(
        String firstName,
        String lastName,

        @Email(message = "Email-ul trebuie să fie valid")
        String email,

        @Min(value = 18, message = "Vârsta minimă este 18 ani")
        Integer age
) {
}

