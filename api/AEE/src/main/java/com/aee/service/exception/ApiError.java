package com.aee.service.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Data
public class ApiError {
    private HttpStatus status;
    private List<String> errors;

    public ApiError(final HttpStatus status, final List<String> errors) {
        super();
        this.status = status;
        this.errors = errors;
    }

    public ApiError(final HttpStatus status, final String error) {
        super();
        this.status = status;
        errors = Arrays.asList(error);
    }
}
