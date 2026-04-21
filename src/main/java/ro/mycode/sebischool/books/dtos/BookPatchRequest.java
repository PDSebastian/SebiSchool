package ro.mycode.sebischool.books.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookPatchRequest {
    @NotBlank(message = "Numele cartii este obligatoriu")
    private String bookName;

    private LocalDateTime createdAt;

}

