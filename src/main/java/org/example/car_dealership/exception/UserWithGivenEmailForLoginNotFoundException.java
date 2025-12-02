package org.example.car_dealership.exception;

public class UserWithGivenEmailForLoginNotFoundException extends RuntimeException {
    public UserWithGivenEmailForLoginNotFoundException(String message) {
        super(message);
    }
}
