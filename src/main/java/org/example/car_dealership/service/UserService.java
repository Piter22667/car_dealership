package org.example.car_dealership.service;

import org.example.car_dealership.dto.UserDetailsResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDetailsResponseDto getUserByEmail(String email);
}
