package org.example.car_dealership.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.car_dealership.model.config.testDrive.TestDriveStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_drive_status_history")
@Data
public class TestDriveStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_drive_id", nullable = false)
    private TestDrive testDrive;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id_who_changed", nullable = false)
    private User userWhoChangedTestDriveStatus;

    @Enumerated(EnumType.STRING)
    private TestDriveStatus status;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

}
