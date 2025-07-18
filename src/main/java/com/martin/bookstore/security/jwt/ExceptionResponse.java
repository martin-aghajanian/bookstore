package com.martin.bookstore.security.jwt;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder(setterPrefix = "with")
public class ExceptionResponse {
    private final HttpStatus status;
    private final String message;
}
