package org.example.car_dealership.controller;

import org.example.car_dealership.dto.UserDetailsResponseDto;
import org.example.car_dealership.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsResponseDto> getUserDetails(Authentication authentication) {
        String email = authentication.getName();
        UserDetailsResponseDto userDetailsDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDetailsDto);
    }
}
