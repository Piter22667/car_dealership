package org.example.car_dealership.controller;

import org.example.car_dealership.dto.OrderForUserDto;
import org.example.car_dealership.dto.TestDriveForUserDto;
import org.example.car_dealership.dto.UserDetailsResponseDto;
import org.example.car_dealership.service.OrderService;
import org.example.car_dealership.service.TestDriveService;
import org.example.car_dealership.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TestDriveService testDriveService;
    private final OrderService orderService;

    public UserController(UserService userService, TestDriveService testDriveService, OrderService orderService) {
        this.userService = userService;
        this.testDriveService = testDriveService;
        this.orderService = orderService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsResponseDto> getUserDetails(Authentication authentication) {
        String email = authentication.getName();
        UserDetailsResponseDto userDetailsDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDetailsDto);
    }

    @GetMapping("/testDrives")
    public ResponseEntity<List<TestDriveForUserDto>> getTestDrivesForUser(Authentication authentication) {
        String email = authentication.getName();
        List<TestDriveForUserDto> testDrives = testDriveService.getTestDrivesForUser(email);
        return ResponseEntity.ok(testDrives);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderForUserDto>> getOrdersForUser(Authentication authentication) {
        String email = authentication.getName();
        List<OrderForUserDto> orders = orderService.getOrdersForUser(email);
        return ResponseEntity.ok(orders);
    }
}
