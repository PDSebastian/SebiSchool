package ro.mycode.sebischool.auth.authService;

import ro.mycode.sebischool.auth.dtos.AuthLoginRequest;
import ro.mycode.sebischool.auth.dtos.AuthLoginResponse;
import ro.mycode.sebischool.auth.dtos.AuthRegisterRequest;
import ro.mycode.sebischool.users.dtos.UserResponse;

public interface AuthService {
    AuthLoginResponse login(AuthLoginRequest request);
    UserResponse register(AuthRegisterRequest request);
}
