package org.example.car_dealership.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleException(MethodArgumentNotValidException ex) {
        Map<String, String> map = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            map.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(CarImageNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCarImageNotFoundException(CarImageNotFoundException ex) {

        log.warn("Car image not found: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(UserWithGivenEmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserWithGivenEmailAlreadyExistsException(UserWithGivenEmailAlreadyExistsException ex) {
        log.warn("User with given email already exists: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(UserWithGivenEmailForLoginNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserWithGivenEmailForLoginNotFoundException(UserWithGivenEmailForLoginNotFoundException ex) {
        log.warn("User with given email for login not found: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(CarNotExistException.class)
    public ResponseEntity<Map<String, String>> handleCarNotExistException(CarNotExistException ex) {
        log.warn("Car not exist: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(InvalidImageCountException.class)
    public ResponseEntity<Map<String, String>> handleInvalidImageCountException(InvalidImageCountException ex) {
        log.warn("Invalid image count: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }


    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFileFormatException(InvalidFileFormatException ex) {
        log.warn("Invalid file format: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(TestDriveNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTestDriveNotFoundException(TestDriveNotFoundException ex) {
        log.warn("Test drive not found: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(CarAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleCarAlreadyExistException(CarAlreadyExistException ex) {
        log.warn("Car already exist: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(CarNotAvailableException.class)
    public ResponseEntity<Map<String, String>> handleCarNotAvailableException(CarNotAvailableException ex) {
        log.warn("Car not available: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(TestDriveAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleTestDriveAlreadyExistsException(TestDriveAlreadyExistsException ex) {
        log.warn("Test drive already exists: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(TestDriveAlreadyScheduledForThisCarException.class)
    public ResponseEntity<Map<String, String>> handleTestDriveAlreadyScheduledForThisCarException(TestDriveAlreadyScheduledForThisCarException ex) {
        log.warn("Test drive already scheduled for this car: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(TestDriveScheduleViolationException.class)
    public ResponseEntity<Map<String, String>> handleTestDriveScheduleViolationException(TestDriveScheduleViolationException ex) {
        log.warn("Test drive schedule violation: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("JSON parsing error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();

        String message = "Invalid request format";
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;
            String fieldName = ife.getPath().isEmpty() ? "unknown" : ife.getPath().get(0).getFieldName();
            message = String.format("Invalid format for field '%s'. Expected format: yyyy-MM-dd'T'HH:mm:ss (e.g., 2026-03-15T10:30:00)", fieldName);
        } else if (ex.getMessage() != null && ex.getMessage().contains("LocalDateTime")) {
            message = "Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm:ss (e.g., 2026-03-15T10:30:00)";
        }

        errors.put("error", message);
        errors.put("details", ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("Type mismatch for parameter: {}", ex.getName());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", String.format("Invalid type for parameter '%s'. Expected type: %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "An unexpected error occurred");
        errors.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFoundException(OrderNotFoundException ex) {
        log.warn("Order not found: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

}
