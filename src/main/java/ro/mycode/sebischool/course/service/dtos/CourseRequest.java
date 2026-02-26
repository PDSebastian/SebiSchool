package ro.mycode.sebischool.course.service.dtos;

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
    @NotBlank(message = "Numele cursului e obligatoriu")
    private String name;

    @NotBlank(message = "Departamentul este obligatoriu")
    private String departament;
}
