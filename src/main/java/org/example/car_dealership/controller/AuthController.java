package org.example.car_dealership.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.car_dealership.dto.AuthenticationRequestDto;
import org.example.car_dealership.dto.AuthenticationResponseDto;
import org.example.car_dealership.dto.RegisterRequestDto;
import org.example.car_dealership.service.authentication.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication and Authorization", description = "Ендпоінти для аутентифікації та авторизації користувачів")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Вхід користувача", description = "Endpoint для аутентифікації користувача в системі")
//    @Tag(name="Автентифікація")
    @PostMapping("/login")
    @SecurityRequirements
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody AuthenticationRequestDto request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @Operation(summary = "Реєстрація нового користувача", description = "Endpoint для реєстрації нового користувача в системі")
//    @Tag(name="Реєстрація")
    @PostMapping("/register")
    @SecurityRequirements
    public ResponseEntity<AuthenticationResponseDto> register(
            @RequestBody RegisterRequestDto request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }
}
