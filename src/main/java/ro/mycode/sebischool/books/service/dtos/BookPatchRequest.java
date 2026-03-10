package ro.mycode.sebischool.books.service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookPatchRequest {
    @NotBlank(message = "Numele cartii este obligatoriu")
    private String bookName;

    private LocalDateTime createdAt;

}
