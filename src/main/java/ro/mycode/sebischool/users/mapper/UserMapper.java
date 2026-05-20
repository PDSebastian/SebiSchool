package ro.mycode.sebischool.users.mapper;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.users.dtos.UserRequest;
import ro.mycode.sebischool.users.dtos.UserResponse;
import ro.mycode.sebischool.users.model.User;
@Component
public class UserMapper {
    public static User toEntity(UserRequest userRequest) {
        if (userRequest == null) {
            return null;
        }
        return User.builder()
                .firstName(userRequest.firstName())
                .lastName(userRequest.lastName())
                .email(userRequest.email())
                .password(userRequest.password())
                .build();

    }
    public static UserResponse toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getEmail(),
                user.getLastName()
        );
    }
}
