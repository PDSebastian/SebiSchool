package ro.mycode.sebischool.auth.authService;

import ro.mycode.sebischool.auth.dtos.UserLoginrequest;
import ro.mycode.sebischool.users.dtos.UserRequest;
import ro.mycode.sebischool.users.dtos.UserResponse;


public interface AuthService {
    UserResponse login(UserLoginrequest userLoginrequest);
    UserResponse register(UserRequest request);
}
