package ro.mycode.sebischool.auth.authController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.mycode.sebischool.auth.authService.AuthService;
import ro.mycode.sebischool.auth.dtos.UserLoginrequest;
import ro.mycode.sebischool.users.dtos.UserRequest;
import ro.mycode.sebischool.users.dtos.UserResponse;

@RestController
@RequestMapping("/api/v2/auth")
@Slf4j
public class AuthController {
    private AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserLoginrequest  request) {
        return ResponseEntity.ok(authService.login(request));
    }



}
