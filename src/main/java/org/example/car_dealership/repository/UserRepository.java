package org.example.car_dealership.repository;

import org.example.car_dealership.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
