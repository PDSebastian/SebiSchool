package ro.mycode.sebischool.course.service.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record CoursePatchRequest(
        @Size(min = 3,max = 50,message = "Numele este obligatoriu")
        String name,
        @Size(min = 3,max = 50,message = "Departamentul este obligatoriu")
        String departament

) {
}
