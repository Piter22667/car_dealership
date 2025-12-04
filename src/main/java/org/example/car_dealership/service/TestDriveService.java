package org.example.car_dealership.service;

import org.example.car_dealership.dto.TestDriveAdminDto;
import org.example.car_dealership.dto.TestDriveForUserDto;
import org.example.car_dealership.dto.TestDriveResponseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface TestDriveService {
    TestDriveResponseDto createTestDrive(String clientEmail, Long carId, LocalDateTime scheduledAt);

    List<TestDriveForUserDto> getTestDrivesForUser(String clientEmail);
    
    List<TestDriveAdminDto> getAllTestDrives();

    TestDriveAdminDto approveTestDrive(Long testDriveId, String adminEmail);

    TestDriveAdminDto cancelTestDrive(Long testDriveId, String adminEmail);

    TestDriveAdminDto completeTestDrive(Long testDriveId, String adminEmail);
}
