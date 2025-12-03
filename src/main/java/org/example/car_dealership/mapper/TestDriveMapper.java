package org.example.car_dealership.mapper;

import org.example.car_dealership.dto.TestDriveAdminDto;
import org.example.car_dealership.dto.TestDriveForUserDto;
import org.example.car_dealership.dto.TestDriveResponseDto;
import org.example.car_dealership.model.TestDrive;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestDriveMapper {

    public TestDriveResponseDto toTestDriveResponseDto(TestDrive testDrive) {
        return TestDriveResponseDto.builder()
                .id(testDrive.getId())
                .carId(testDrive.getCar().getId())
                .currentStatus(testDrive.getCurrentStatus())
                .clientId(testDrive.getUser().getId())
                .scheduledAt(testDrive.getScheduledAt())
                .build();
    }

    public List<TestDriveForUserDto> toTestDriveForUserDtoList(List<TestDrive> testDrives) {
        return testDrives.stream()
                .map(testDrive -> TestDriveForUserDto.builder()
                        .id(testDrive.getId())
                        .currentStatus(testDrive.getCurrentStatus())
                        .scheduledAt(testDrive.getScheduledAt())
                        .userId(testDrive.getUser().getId())
                        .car(new TestDriveForUserDto.CarDto(testDrive.getCar().getId(), testDrive.getCar().getBrand(), testDrive.getCar().getModel()))
                        .build()).toList();
    }

    public TestDriveAdminDto toTestDriveAdminDto(TestDrive testDrive) {
        return TestDriveAdminDto.builder()
                .id(testDrive.getId())
                .currentStatus(testDrive.getCurrentStatus())
                .scheduledAt(testDrive.getScheduledAt())
                .lastChangedStatusAt(testDrive.getLastChangedStatusAt())
                .user(new TestDriveAdminDto.UserDto(
                        testDrive.getUser().getId(),
                        testDrive.getUser().getEmail(),
                        testDrive.getUser().getName()
                ))
                .car(new TestDriveAdminDto.CarDto(
                        testDrive.getCar().getId(),
                        testDrive.getCar().getBrand(),
                        testDrive.getCar().getModel(),
                        testDrive.getCar().getYear().toString()
                ))
                .build();
    }

    public List<TestDriveAdminDto> toTestDriveAdminDtoList(List<TestDrive> testDrives) {
        return testDrives.stream()
                .map(this::toTestDriveAdminDto)
                .toList();
    }
}
