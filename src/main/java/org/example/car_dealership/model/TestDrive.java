package org.example.car_dealership.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.car_dealership.model.config.testDrive.TestDriveStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "test_drives")
@Data
public class TestDrive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    private TestDriveStatus currentStatus;

    private LocalDateTime lastChangedStatusAt;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude // вказуємо для уникнення рекурсї між звязками та додаткового завантаження toString через анотації lombok
    @OneToMany(mappedBy = "testDrive")
    private List<TestDriveStatusHistory> testDriveStatusHistory;
}