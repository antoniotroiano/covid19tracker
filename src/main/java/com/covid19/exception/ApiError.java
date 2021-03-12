package com.covid19.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ApiError {

    private final HttpStatus httpStatusCode;
    private final String errorMessage;

    public ApiError(@NonNull HttpStatus httpStatusCode, @NonNull String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
    }
}