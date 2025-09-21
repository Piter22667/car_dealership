package org.example.car_dealership.repository;

import org.example.car_dealership.model.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarImageRepository extends JpaRepository<CarImage, Long> {
}
