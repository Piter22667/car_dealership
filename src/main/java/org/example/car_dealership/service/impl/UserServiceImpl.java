package org.example.car_dealership.service.impl;

import org.example.car_dealership.dto.UserDetailsResponseDto;
import org.example.car_dealership.mapper.UserMapper;
import org.example.car_dealership.model.User;
import org.example.car_dealership.repository.UserRepository;
import org.example.car_dealership.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    public UserDetailsResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return userMapper.toUserDetailsResponseDto(user);
    }
}
