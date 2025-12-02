package org.example.car_dealership.repository;

import jakarta.persistence.LockModeType;
import org.example.car_dealership.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Car c WHERE c.id = :carId")
    Optional<Car> findByIdWithLock(@Param("carId") Long carId);


    boolean existsByModelAndBrand(String model, String brand);

    boolean existsByRegistrationNumber(String registrationNumber);
}
