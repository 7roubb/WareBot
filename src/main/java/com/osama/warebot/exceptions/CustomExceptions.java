package com.osama.warebot.exceptions;

public class CustomExceptions {

    // ===== Product exceptions =====
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }

    public static class ProductAlreadyExistsException extends RuntimeException {
        public ProductAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class InvalidCategoryException extends RuntimeException {
        public InvalidCategoryException(String message) {
            super(message);
        }
    }

    public static class OutOfStockException extends RuntimeException {
        public OutOfStockException(String message) {
            super(message);
        }
    }

    public static class InvalidDiscountException extends RuntimeException {
        public InvalidDiscountException(String message) {
            super(message);
        }
    }

    public static class InvalidLocalizedFieldException extends RuntimeException {
        public InvalidLocalizedFieldException(String message) {
            super(message);
        }
    }

    public static class ProductValidationException extends RuntimeException {
        public ProductValidationException(String message) {
            super(message);
        }
    }

    public static class ImageProcessingException extends RuntimeException {
        public ImageProcessingException(String message) {
            super(message);
        }
    }

    public static class ImageStorageException extends RuntimeException {
        public ImageStorageException(String message) {
            super(message);
        }
    }

    // ===== Shelf exceptions =====
    public static class ShelfNotFoundException extends RuntimeException {
        public ShelfNotFoundException(String id) {
            super(id);
        }
    }

    public static class ShelfAlreadyExistsException extends RuntimeException {
        public ShelfAlreadyExistsException(String message) {
            super(message);
        }
    }

    // ===== Robot exceptions =====
    public static class RobotNotFoundException extends RuntimeException {
        public RobotNotFoundException() {
            super();
        }
    }

    public static class RobotBusyException extends RuntimeException {
        public RobotBusyException(String id) {
            super(id);
        }
    }

    public static class RobotUnavailableException extends RuntimeException {
        public RobotUnavailableException(String id) {
            super(id);
        }
    }

    public static class RobotAlreadyExistsException extends RuntimeException {
        public RobotAlreadyExistsException() {
            super();
        }
    }

    // ===== New Robot Telemetry exceptions =====
    public static class TelemetryNotFoundException extends RuntimeException {
        public TelemetryNotFoundException() {
            super();
        }
    }

    public static class TelemetrySaveException extends RuntimeException {
        public TelemetrySaveException(String message) {
            super(message);
        }
    }

    public static class InvalidTelemetryDataException extends RuntimeException {
        public InvalidTelemetryDataException(String message) {
            super(message);
        }
    }

    // ===== New Robot Status exceptions =====
    public static class InvalidStatusException extends RuntimeException {
        public InvalidStatusException() {
            super();
        }
    }

    public static class RobotStatusUpdateException extends RuntimeException {
        public RobotStatusUpdateException(String message) {
            super(message);
        }
    }

    // ===== New MQTT exceptions =====
    public static class MqttConnectionException extends RuntimeException {
        public MqttConnectionException(String message) {
            super(message);
        }
    }

    public static class MqttMessageProcessingException extends RuntimeException {
        public MqttMessageProcessingException(String message) {
            super(message);
        }
    }

    public static class InvalidMqttMessageException extends RuntimeException {
        public InvalidMqttMessageException(String message) {
            super(message);
        }
    }

    public static class MqttSubscriptionException extends RuntimeException {
        public MqttSubscriptionException(String message) {
            super(message);
        }
    }

    // ===== New Robot Assignment exceptions =====
    public static class RobotAssignmentException extends RuntimeException {
        public RobotAssignmentException(String message) {
            super(message);
        }
    }

    public static class ShelfAlreadyOccupiedException extends RuntimeException {
        public ShelfAlreadyOccupiedException(String message) {
            super(message);
        }
    }

    public static class RobotNotAssignedException extends RuntimeException {
        public RobotNotAssignedException(String message) {
            super(message);
        }
    }

    // ===== New Monitoring exceptions =====
    public static class MetricsCollectionException extends RuntimeException {
        public MetricsCollectionException(String message) {
            super(message);
        }
    }

    public static class GrafanaIntegrationException extends RuntimeException {
        public GrafanaIntegrationException(String message) {
            super(message);
        }
    }

    public static class PrometheusMetricsException extends RuntimeException {
        public PrometheusMetricsException(String message) {
            super(message);
        }
    }

    // ===== New Database exceptions =====
    public static class DatabaseConnectionException extends RuntimeException {
        public DatabaseConnectionException(String message) {
            super(message);
        }
    }

    public static class DataAccessException extends RuntimeException {
        public DataAccessException(String message) {
            super(message);
        }
    }

    // ===== New Validation exceptions =====
    public static class InvalidRobotDataException extends RuntimeException {
        public InvalidRobotDataException(String message) {
            super(message);
        }
    }

    public static class RobotValidationException extends RuntimeException {
        public RobotValidationException(String message) {
            super(message);
        }
    }
}