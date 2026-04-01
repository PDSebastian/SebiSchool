package ro.mycode.sebischool.system.exceptions;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record ApiErrorResponse(
        LocalDateTime dateTime,
        int state,
        String message
) {
}
