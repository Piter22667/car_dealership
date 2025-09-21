package org.example.car_dealership.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "test_drives")
@Data
public class TestDrive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;


    @OneToMany(mappedBy = "testDrive", cascade = CascadeType.ALL)
    private List<TestDriveStatusHistory> testDriveStatusHistory;
}