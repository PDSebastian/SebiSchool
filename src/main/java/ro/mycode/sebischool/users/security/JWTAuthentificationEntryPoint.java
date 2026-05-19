package ro.mycode.sebischool.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ro.mycode.sebischool.system.exceptions.ApiErrorResponse;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Component
public class JWTAuthentificationEntryPoint {
    private ObjectMapper objectMapper;
    public JWTAuthentificationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

    }
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiErrorResponse errorResponse = ApiErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                "Trebuie sa fii autentificat pentru a accesa aceasta resursa.",
                request.getRequestURI()
        );
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

}
