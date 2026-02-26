package ro.mycode.sebischool.books.service.dtos;

import jakarta.validation.constraints.NotBlank;
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
public class Bookrequest {
    @NotBlank(message = "Numele cartii este obligatoriu")
    private String bookName;

    @NotNull(message = "Data este obligatorie")
    private LocalDateTime createdAt;
}
