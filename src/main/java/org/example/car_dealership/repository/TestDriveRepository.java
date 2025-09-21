package org.example.car_dealership.repository;

import org.example.car_dealership.model.TestDrive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDriveRepository extends JpaRepository<TestDrive, Long> {
}
