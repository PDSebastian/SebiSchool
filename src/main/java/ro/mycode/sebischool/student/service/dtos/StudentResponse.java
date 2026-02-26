package ro.mycode.sebischool.student.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class StudentResponse {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private int phoneNumber;
}
