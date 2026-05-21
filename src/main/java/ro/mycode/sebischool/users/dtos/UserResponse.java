package ro.mycode.sebischool.users.dtos;

import java.security.Permission;
import java.util.Set;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String token
) {
}
