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

}
