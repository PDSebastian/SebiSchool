package ro.mycode.sebischool.course.dtos;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CoursePatchRequest(
        @Size(min = 3,max = 50,message = "Numele este obligatoriu")
        String name,
        @Size(min = 3,max = 50,message = "Departamentul este obligatoriu")
        String departament

) {
}
