package org.example.car_dealership.repository;

import org.example.car_dealership.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    boolean existsAllByModelAndBrand(String model, String brand);

    boolean existsByRegistrationNumber(@Param("regNumber") String registrationNumber);
}
