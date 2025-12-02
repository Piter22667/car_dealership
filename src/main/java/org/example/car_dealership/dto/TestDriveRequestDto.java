package org.example.car_dealership.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Request DTO for scheduling a test drive")
public class TestDriveRequestDto {
    
    @NotNull(message = "Scheduled time is required")
    @Future(message = "Scheduled time must be in the future")
    @Schema(description = "The date and time when the test drive is scheduled", example = "2024-12-15T10:00:00")
    private LocalDateTime scheduledAt;
}
