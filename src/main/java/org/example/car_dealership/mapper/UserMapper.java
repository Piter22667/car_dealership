package org.example.car_dealership.mapper;

import org.example.car_dealership.dto.UserDetailsResponseDto;
import org.example.car_dealership.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDetailsResponseDto toUserDetailsResponseDto(User user) {
        UserDetailsResponseDto dto = new UserDetailsResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPhone(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setAddress(user.getAddress());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
