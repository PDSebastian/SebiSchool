package ro.mycode.sebischool.course.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRequest {
    Long id;
    @NotBlank(message = "Numele cursului e obligatoriu")
    private String name;

    @NotBlank(message = "Departamentul este obligatoriu")
    private String departament;
}
