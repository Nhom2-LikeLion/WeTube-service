package com.wetube.wetube_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidClerkTokenException extends RuntimeException{
    public InvalidClerkTokenException(String message) {
        super(message);
    }

    public InvalidClerkTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
