package org.example.car_dealership.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.car_dealership.model.config.testDrive.TestDriveStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TestDriveResponseDto {
    private Long id;
    private Long carId;
    private TestDriveStatus currentStatus;
    private Long clientId;
    private LocalDateTime scheduledAt;
}
