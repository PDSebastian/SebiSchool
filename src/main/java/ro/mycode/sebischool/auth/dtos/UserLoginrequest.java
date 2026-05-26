package ro.mycode.sebischool.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginrequest(
        @Email
        @NotBlank
        @Size(min = 3, max = 50)
        String email,

        @NotBlank
        @Size(min = 3, max = 50)
        String password




) {
}
