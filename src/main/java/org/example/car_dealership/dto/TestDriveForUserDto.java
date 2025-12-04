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
public class TestDriveForUserDto {
    private Long id;
    private TestDriveStatus currentStatus;
    private LocalDateTime scheduledAt;
    private Long userId;
    private CarDto car;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CarDto{
        private Long carId;
        private String brand;
        private String model;
    }
}
