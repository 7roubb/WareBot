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

    @ExceptionHandler(CustomExceptions.InvalidCategoryException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCategory(CustomExceptions.InvalidCategoryException ex) {
        log.warn("Invalid category: {}", ex.getMessage());
        return buildErrorResponse("product.invalid.category", HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.OutOfStockException.class)
    public ResponseEntity<ApiResponse<Void>> handleOutOfStock(CustomExceptions.OutOfStockException ex) {
        log.warn("Product out of stock: {}", ex.getMessage());
        return buildErrorResponse("product.out.of.stock", HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.InvalidDiscountException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidDiscount(CustomExceptions.InvalidDiscountException ex) {
        log.warn("Invalid discount: {}", ex.getMessage());
        return buildErrorResponse("product.invalid.discount", HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.InvalidLocalizedFieldException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidLocalizedField(CustomExceptions.InvalidLocalizedFieldException ex) {
        log.warn("Invalid localized field: {}", ex.getMessage());
        return buildErrorResponse("product.invalid.localized.field", HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.ProductValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductValidation(CustomExceptions.ProductValidationException ex) {
        log.warn("Product validation failed: {}", ex.getMessage());
        return buildErrorResponse("product.validation.failed", HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.ImageProcessingException.class)
    public ResponseEntity<ApiResponse<Void>> handleImageProcessing(CustomExceptions.ImageProcessingException ex) {
        log.error("Image processing error: {}", ex.getMessage());
        return buildErrorResponse("image.processing.error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.ImageStorageException.class)
    public ResponseEntity<ApiResponse<Void>> handleImageStorage(CustomExceptions.ImageStorageException ex) {
        log.error("Image storage error: {}", ex.getMessage());
        return buildErrorResponse("image.storage.error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
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

    // ===== Robot Telemetry exceptions =====
    @ExceptionHandler(CustomExceptions.TelemetryNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTelemetryNotFound(CustomExceptions.TelemetryNotFoundException ex) {
        log.error("Telemetry data not found");
        return buildErrorResponse("robot.telemetry.not.found", HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(CustomExceptions.TelemetrySaveException.class)
    public ResponseEntity<ApiResponse<Void>> handleTelemetrySave(CustomExceptions.TelemetrySaveException ex) {
        log.error("Telemetry save failed: {}", ex.getMessage());
        return buildErrorResponse("robot.telemetry.save.failed", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.InvalidTelemetryDataException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidTelemetryData(CustomExceptions.InvalidTelemetryDataException ex) {
        log.warn("Invalid telemetry data: {}", ex.getMessage());
        return buildErrorResponse("robot.invalid.telemetry.data", HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ===== Robot Status exceptions =====
    @ExceptionHandler(CustomExceptions.InvalidStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidStatus(CustomExceptions.InvalidStatusException ex) {
        log.warn("Invalid robot status: {}", ex.getMessage());
        return buildErrorResponse("robot.invalid.status", HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.RobotStatusUpdateException.class)
    public ResponseEntity<ApiResponse<Void>> handleRobotStatusUpdate(CustomExceptions.RobotStatusUpdateException ex) {
        log.error("Robot status update failed: {}", ex.getMessage());
        return buildErrorResponse("robot.status.update.failed", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    // ===== MQTT exceptions =====
    @ExceptionHandler(CustomExceptions.MqttConnectionException.class)
    public ResponseEntity<ApiResponse<Void>> handleMqttConnection(CustomExceptions.MqttConnectionException ex) {
        log.error("MQTT connection failed: {}", ex.getMessage());
        return buildErrorResponse("mqtt.connection.failed", HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.MqttMessageProcessingException.class)
    public ResponseEntity<ApiResponse<Void>> handleMqttMessageProcessing(CustomExceptions.MqttMessageProcessingException ex) {
        log.error("MQTT message processing failed: {}", ex.getMessage());
        return buildErrorResponse("mqtt.message.processing.failed", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.InvalidMqttMessageException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidMqttMessage(CustomExceptions.InvalidMqttMessageException ex) {
        log.warn("Invalid MQTT message: {}", ex.getMessage());
        return buildErrorResponse("mqtt.invalid.message.format", HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.MqttSubscriptionException.class)
    public ResponseEntity<ApiResponse<Void>> handleMqttSubscription(CustomExceptions.MqttSubscriptionException ex) {
        log.error("MQTT subscription failed: {}", ex.getMessage());
        return buildErrorResponse("mqtt.subscription.failed", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    // ===== Robot Assignment exceptions =====
    @ExceptionHandler(CustomExceptions.RobotAssignmentException.class)
    public ResponseEntity<ApiResponse<Void>> handleRobotAssignment(CustomExceptions.RobotAssignmentException ex) {
        log.warn("Robot assignment failed: {}", ex.getMessage());
        return buildErrorResponse("robot.assignment.failed", HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.ShelfAlreadyOccupiedException.class)
    public ResponseEntity<ApiResponse<Void>> handleShelfAlreadyOccupied(CustomExceptions.ShelfAlreadyOccupiedException ex) {
        log.warn("Shelf already occupied: {}", ex.getMessage());
        return buildErrorResponse("shelf.already.occupied", HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.RobotNotAssignedException.class)
    public ResponseEntity<ApiResponse<Void>> handleRobotNotAssigned(CustomExceptions.RobotNotAssignedException ex) {
        log.warn("Robot not assigned: {}", ex.getMessage());
        return buildErrorResponse("robot.not.assigned", HttpStatus.BAD_REQUEST, ex.getMessage());
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