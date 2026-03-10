package ro.mycode.sebischool.student.service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentPatchRequest {
    private String firstName;
    private String lastName;
    private String email;

    private Integer age;

}
