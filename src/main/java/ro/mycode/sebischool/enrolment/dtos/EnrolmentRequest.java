package ro.mycode.sebischool.enrolment.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrolmentRequest {
    @NotNull(message = "ID-ul cursului este obligatoriu")
    @Min(value = 1, message = "ID-ul cursului trebuie să fie mai mare decât 0")
    private Long courseId;

    @NotNull(message = "ID-ul studentului este obligatoriu")
    @Min(value = 1, message = "ID-ul studentului trebuie să fie mai mare decât 0")
    private Long studentId;


}
