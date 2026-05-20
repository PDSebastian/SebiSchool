package ro.mycode.sebischool.auth.dtos;

import ro.mycode.sebischool.users.security.UserPermissions;

import java.util.Set;

public record AuthLoginResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Set<UserPermissions> directPermissions,
        String token




) {
}
