package org.example.car_dealership.repository;

import org.example.car_dealership.model.ServiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceHistoryRepository extends JpaRepository<ServiceHistory, Long> {
}
