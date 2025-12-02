package org.example.car_dealership.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.car_dealership.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(@Size(min = 5, max = 100, message = "Електронна пошта повинна містити від 5 до 100 символів") @NotBlank(message = "Електронна пошта не може бути порожньою") @Email(message = "Пошта має бути у форматі user@example.com") String email);
//    boolean existsByUsername(String username);
//    boolean existsByEmail(String email);
}
