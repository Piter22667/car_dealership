package org.example.car_dealership.service.authentication;

import lombok.RequiredArgsConstructor;
import org.example.car_dealership.dto.AuthenticationRequestDto;
import org.example.car_dealership.dto.AuthenticationResponseDto;
import org.example.car_dealership.dto.RegisterRequestDto;
import org.example.car_dealership.exception.UserWithGivenEmailAlreadyExistsException;
import org.example.car_dealership.exception.UserWithGivenEmailForLoginNotFoundException;
import org.example.car_dealership.model.User;
import org.example.car_dealership.model.config.user.Role;
import org.example.car_dealership.repository.UserRepository;
import org.example.car_dealership.service.authentication.config.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

//    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtService = jwtService;
//        this.authenticationManager = authenticationManager;
//    }


    public AuthenticationResponseDto register(RegisterRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserWithGivenEmailAlreadyExistsException("User with given email already exists");
        }

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .id(user.getId())
                .role(user.getRole().name())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    } // зберігаємо користувача в базі даних, генеруємо JWT токен і повертаємо його

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserWithGivenEmailForLoginNotFoundException("User not found with given email."));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .id(user.getId())
                .role(user.getRole().name())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }// аутентифікуємо користувача за email та паролем і повертаємо токен
}

