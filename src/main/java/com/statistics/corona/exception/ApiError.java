package com.statistics.corona.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {

    private final HttpStatus httpStatusCode;
    private final String errorMessage;

    public ApiError(@NonNull HttpStatus httpStatusCode, @NonNull String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
    }
}