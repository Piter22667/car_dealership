package org.example.car_dealership.model.config.car;

public enum CarStatus {
    AVAILABLE,
    RESERVED_PENDING, //в процесі бронювання для покупки
    RESERVED, //заброньовано для покупки
    SOLD,
//    ON_TEST_DRIVE TODO чи додавати статус на тест драйві?
}
