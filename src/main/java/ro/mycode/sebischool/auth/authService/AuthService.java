package ro.mycode.sebischool.auth.authService;

import ro.mycode.sebischool.student.dtos.StudentDetailResponse;
import ro.mycode.sebischool.student.dtos.StudentRequest;
import ro.mycode.sebischool.auth.dtos.AuthLoginRequest;
import ro.mycode.sebischool.auth.dtos.AuthLoginResponse;
import ro.mycode.sebischool.users.dtos.UserRequest;
import ro.mycode.sebischool.users.dtos.UserResponse;

public interface AuthService {
    AuthLoginResponse login(UserRequest request);
    UserResponse register(UserRequest request);
}
