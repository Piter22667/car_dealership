package org.example.car_dealership.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.car_dealership.model.config.user.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsResponseDto {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private String email;
    private Role role;
}
