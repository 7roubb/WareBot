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
                .message(getMessage("validation.failed", new Object[]{ex.getMessage()}))
                .content(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomExceptions.ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductNotFound(CustomExceptions.ProductNotFoundException ex) {
        log.error("Product not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(getMessage("product.not.found", new Object[]{ex.getMessage()}), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(CustomExceptions.ProductAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductAlreadyExists(CustomExceptions.ProductAlreadyExistsException ex) {
        log.warn("Product already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(getMessage("product.already.exists", new Object[]{ex.getMessage()}), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(CustomExceptions.InvalidCategoryException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCategory(CustomExceptions.InvalidCategoryException ex) {
        log.warn("Invalid category: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(getMessage("invalid.category", new Object[]{ex.getMessage()}), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(CustomExceptions.OutOfStockException.class)
    public ResponseEntity<ApiResponse<Void>> handleOutOfStock(CustomExceptions.OutOfStockException ex) {
        log.warn("Product out of stock: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(getMessage("out.of.stock", new Object[]{ex.getMessage()}), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(CustomExceptions.InvalidDiscountException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidDiscount(CustomExceptions.InvalidDiscountException ex) {
        log.warn("Invalid discount: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(getMessage("invalid.discount", new Object[]{ex.getMessage()}), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(CustomExceptions.ProductValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationError(CustomExceptions.ProductValidationException ex) {
        log.warn("Product validation error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(getMessage("product.validation.failed", new Object[]{ex.getMessage()}), HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @ExceptionHandler(CustomExceptions.InvalidLocalizedFieldException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidLocalizedField(CustomExceptions.InvalidLocalizedFieldException ex) {
        log.warn("Invalid localized field: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(getMessage("invalid.localized.field", new Object[]{ex.getMessage()}), HttpStatus.BAD_REQUEST));
    }
    @ExceptionHandler(CustomExceptions.ImageProcessingException.class)
    public ResponseEntity<ApiResponse<Void>> handleImageProcessingException(CustomExceptions.ImageProcessingException ex) {
        log.warn("Image processing error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(getMessage("image.processing.error", new Object[]{ex.getMessage()}), HttpStatus.BAD_REQUEST));
    }
    @ExceptionHandler(CustomExceptions.ImageStorageException.class)
    public ResponseEntity<ApiResponse<Void>> handleImageStorageException(CustomExceptions.ImageStorageException ex) {
        log.warn("Image storage error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(getMessage("image.upload.failed", new Object[]{ex.getMessage()}), HttpStatus.BAD_REQUEST));
    }

    private String getMessage(String messageKey, Object[] args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }
}
