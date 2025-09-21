package org.example.car_dealership.repository;

import org.example.car_dealership.model.TestDriveStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDriveStatusHistoryRepository extends JpaRepository<TestDriveStatusHistory, Long> {
}
