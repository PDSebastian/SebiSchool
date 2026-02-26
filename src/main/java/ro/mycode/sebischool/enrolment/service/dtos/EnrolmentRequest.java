package ro.mycode.sebischool.enrolment.service.dtos;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Long courseId;
    @NotNull(message = "Data este obligatorie")
    private LocalDateTime createdAt;
}
