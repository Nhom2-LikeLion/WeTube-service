package com.wetube.wetube_service.exception.handler;

import com.wetube.wetube_service.exception.InvalidTokenException;
import com.wetube.wetube_service.exception.error.ApiError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.HtmlUtils;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest req) {
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        String safeDetails = HtmlUtils.htmlEscape(details);
        String safePath = HtmlUtils.htmlEscape(req.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(HttpStatus.BAD_REQUEST, safeDetails, safePath));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex,
                                                   HttpServletRequest req) {
        String safeMessage = HtmlUtils.htmlEscape(ex.getMessage());
        String safePath = HtmlUtils.htmlEscape(req.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(HttpStatus.NOT_FOUND, safeMessage, safePath));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleConflict(IllegalStateException ex,
                                                   HttpServletRequest req) {
        String safeMessage = HtmlUtils.htmlEscape(ex.getMessage());
        String safePath = HtmlUtils.htmlEscape(req.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError(HttpStatus.CONFLICT, safeMessage, safePath));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnknown(Exception ex,
                                                  HttpServletRequest req) {
        String safePath = HtmlUtils.htmlEscape(req.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", safePath));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleUnauthorized(InvalidTokenException ex,
                                                       HttpServletRequest req) {
        String safeMessage = HtmlUtils.htmlEscape(ex.getMessage());
        String safePath = HtmlUtils.htmlEscape(req.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(HttpStatus.UNAUTHORIZED, safeMessage, safePath));
    }
}
