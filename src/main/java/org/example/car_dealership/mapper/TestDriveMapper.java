package org.example.car_dealership.mapper;

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
}
