package org.example.car_dealership.repository;

import org.example.car_dealership.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
