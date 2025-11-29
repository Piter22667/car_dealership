package org.example.car_dealership.exception;

public class CarNotExistException extends RuntimeException {
    public CarNotExistException(String message) {
        super(message);
    }
}
