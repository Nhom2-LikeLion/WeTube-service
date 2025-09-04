package com.wetube.wetube_service.exception.handler;

import com.wetube.wetube_service.dto.response.ErrorResponse;
import com.wetube.wetube_service.exception.InvalidTokenException;
import com.wetube.wetube_service.exception.ResourceNotFoundException;
import com.wetube.wetube_service.exception.error.ApiError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
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


    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleUnauthorized(InvalidTokenException ex,
                                                       HttpServletRequest req) {
        String safeMessage = HtmlUtils.htmlEscape(ex.getMessage());
        String safePath = HtmlUtils.htmlEscape(req.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(HttpStatus.UNAUTHORIZED, safeMessage, safePath));
    }

    // 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }

    // 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 500 - Internal Server Error (các lỗi khác chưa bắt)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                "Something went wrong: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
