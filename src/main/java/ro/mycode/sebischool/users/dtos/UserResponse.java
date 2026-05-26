package ro.mycode.sebischool.users.dtos;



public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String token
) {
}
