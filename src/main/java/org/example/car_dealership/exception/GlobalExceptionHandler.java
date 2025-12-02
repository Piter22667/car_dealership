package org.example.car_dealership.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCarNotFoundException(CarNotFoundException ex) {
        log.warn("Car not found: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(TestDriveAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleTestDriveAlreadyExistsException(TestDriveAlreadyExistsException ex) {
        log.warn("Test drive already exists: {}", ex.getMessage());
    @ExceptionHandler(InvalidImageCountException.class)
    public ResponseEntity<Map<String, String>> handleInvalidImageCountException(InvalidImageCountException ex) {
        log.warn("Invalid image count: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(TestDriveAlreadyScheduledForThisCarException.class)
    public ResponseEntity<Map<String, String>> handleTestDriveAlreadyScheduledForThisCarException(TestDriveAlreadyScheduledForThisCarException ex) {
        log.warn("Test drive already scheduled for this car: {}", ex.getMessage());
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

    @ExceptionHandler(TestDriveScheduleViolationException.class)
    public ResponseEntity<Map<String, String>> handleTestDriveScheduleViolationException(TestDriveScheduleViolationException ex) {
        log.warn("Test drive schedule violation: {}", ex.getMessage());
    @ExceptionHandler(CarAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleCarAlreadyExistException(CarAlreadyExistException ex) {
        log.warn("Car already exist: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
