package org.example.car_dealership.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CarImageNotFoundException extends RuntimeException {
    public CarImageNotFoundException(String message) {
        super(message);
    }
}
