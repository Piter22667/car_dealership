package org.example.car_dealership.repository;

import org.example.car_dealership.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
