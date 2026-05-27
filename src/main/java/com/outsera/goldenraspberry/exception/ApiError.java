package com.outsera.goldenraspberry.exception;

import lombok.With;

import java.time.OffsetDateTime;
import java.util.List;

@With
public record ApiError(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        List<String> details) {

    public static ApiError of(int status, String error, String message, List<String> details) {
        return new ApiError(OffsetDateTime.now(), status, error, message, details);
    }
}
