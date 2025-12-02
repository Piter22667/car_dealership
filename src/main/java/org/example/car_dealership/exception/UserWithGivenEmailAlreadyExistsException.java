package org.example.car_dealership.exception;

public class UserWithGivenEmailAlreadyExistsException extends RuntimeException {
    public UserWithGivenEmailAlreadyExistsException(String message) {
        super(message);
    }
}
