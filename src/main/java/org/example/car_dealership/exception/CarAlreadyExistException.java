package org.example.car_dealership.exception;

public class CarAlreadyExistException extends RuntimeException {
    public CarAlreadyExistException(String message) {
        super(message);
    }
}
