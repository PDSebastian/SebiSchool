package ro.mycode.sebischool.system.exceptions;

import lombok.Builder;

import java.time.Instant;


@Builder
public record ApiErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(Instant.now().toString(), status, error, message, path);
    }
}
