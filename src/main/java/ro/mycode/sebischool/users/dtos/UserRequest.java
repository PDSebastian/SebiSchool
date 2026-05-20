package ro.mycode.sebischool.users.dtos;

public record UserRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
