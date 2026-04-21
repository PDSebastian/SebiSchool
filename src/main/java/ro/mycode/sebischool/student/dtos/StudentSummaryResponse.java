package ro.mycode.sebischool.student.dtos;

import lombok.*;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Service
@Getter
public class StudentSummaryResponse {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
}
