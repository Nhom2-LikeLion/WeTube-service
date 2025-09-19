package com.wetube.wetube_service.exception.handler;

import com.wetube.wetube_service.exception.*;
import com.wetube.wetube_service.exception.error.ApiError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* -------------------- 400 BAD REQUEST -------------------- */

    // Handle validation errors (e.g., @NotNull, @Size)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest req) {
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, details, req.getRequestURI());
    }

    // Handle bad arguments (e.g., wrong enum, negative id)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex,
                                                          HttpServletRequest req) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    /* -------------------- 401 UNAUTHORIZED -------------------- */

    // Handle invalid or expired token
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleUnauthorized(InvalidTokenException ex,
                                                       HttpServletRequest req) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), req.getRequestURI());
    }

    /* -------------------- 404 NOT FOUND -------------------- */

    // Handle missing entity (JPA)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex,
                                                         HttpServletRequest req) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
    }

    // Handle missing resource (custom)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex,
                                                           HttpServletRequest req) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
    }

    /* -------------------- 409 CONFLICT -------------------- */

    // Handle invalid state in business logic
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(IllegalStateException ex,
                                                       HttpServletRequest req) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI());
    }

    // Handle duplicate channel creation
    @ExceptionHandler(ChannelAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleChannelAlreadyExists(ChannelAlreadyExistsException ex,
                                                               HttpServletRequest req) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI());
    }

    // Handle duplicate resource (e.g., email, username)
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateResource(DuplicateResourceException ex,
                                                            HttpServletRequest req) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI());
    }

    // Handle attempt to delete default tier
    @ExceptionHandler(DefaultTierException.class)
    public ResponseEntity<ApiError> handleDefaultTierDeletion(DefaultTierException ex,
                                                              HttpServletRequest req) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI());
    }

    /* -------------------- 500 INTERNAL SERVER ERROR -------------------- */

    // Handle all uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOtherExceptions(Exception ex,
                                                          HttpServletRequest req) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong: " + ex.getMessage(),
                req.getRequestURI());
    }

    /* -------------------- Utility -------------------- */

    // Build consistent API error response
    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, String path) {
        return ResponseEntity.status(status).body(new ApiError(status, message, path));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Map.of("success", false, "message", "File is too large! Maximum size is 10MB."));
    }
}
