package com.wetube.wetube_service.exception;

public class InvalidClerkTokenException extends RuntimeException{
    public InvalidClerkTokenException(String message) {
        super(message);
    }

    public InvalidClerkTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}