package com.osama.warebot.exceptions;


import com.osama.warebot.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    // ===== Validation exceptions =====
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("Validation failed: {}", errors);

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(getMessage("validation.failed", null))
                .content(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ===== Product exceptions =====
    @ExceptionHandler(CustomExceptions.ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductNotFound(CustomExceptions.ProductNotFoundException ex) {
        log.error("Product not found: {}", ex.getMessage());
        return buildErrorResponse("product.not.found", HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.ProductAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductAlreadyExists(CustomExceptions.ProductAlreadyExistsException ex) {
        log.warn("Product already exists: {}", ex.getMessage());
        return buildErrorResponse("product.already.exists", HttpStatus.CONFLICT, ex.getMessage());
    }

    // ===== Shelf exceptions =====
    @ExceptionHandler(CustomExceptions.ShelfNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleShelfNotFound(CustomExceptions.ShelfNotFoundException ex) {
        log.error("Shelf not found: {}", ex.getMessage());
        return buildErrorResponse("shelf.not.found", HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.ShelfAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleShelfAlreadyExists(CustomExceptions.ShelfAlreadyExistsException ex) {
        log.warn("Shelf already exists: {}", ex.getMessage());
        return buildErrorResponse("shelf.already.exists", HttpStatus.CONFLICT, ex.getMessage());
    }

    // ===== Robot exceptions =====
    @ExceptionHandler(CustomExceptions.RobotNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRobotNotFound(CustomExceptions.RobotNotFoundException ex) {
        log.error("Robot not found");
        return buildErrorResponse("robot.not.found", HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(CustomExceptions.RobotBusyException.class)
    public ResponseEntity<ApiResponse<Void>> handleRobotBusy(CustomExceptions.RobotBusyException ex) {
        log.warn("Robot busy: {}", ex.getMessage());
        return buildErrorResponse("robot.busy", HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.RobotUnavailableException.class)
    public ResponseEntity<ApiResponse<Void>> handleRobotUnavailable(CustomExceptions.RobotUnavailableException ex) {
        log.warn("Robot unavailable: {}", ex.getMessage());
        return buildErrorResponse("robot.unavailable", HttpStatus.LOCKED, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.RobotAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleRobotAlreadyExists(CustomExceptions.RobotAlreadyExistsException ex) {
        log.warn("Robot already exists");
        return buildErrorResponse("robot.already.exists", HttpStatus.CONFLICT, null);
    }

    // ===== Unhandled exceptions =====
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllUnhandledExceptions(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return buildErrorResponse("internal.server.error", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    // ===== Helper method =====
    private ResponseEntity<ApiResponse<Void>> buildErrorResponse(String messageKey, HttpStatus status, String arg) {
        String message = getMessage(messageKey, arg != null ? new Object[]{arg} : null);
        ApiResponse<Void> response = ApiResponse.error(message, status);
        return new ResponseEntity<>(response, status);
    }

    private String getMessage(String messageKey, Object[] args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }
}
