package org.example.car_dealership.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "service_history")
public class ServiceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "service_date")
    private LocalDateTime serviceDate;

    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal mileage;

    @Column(precision = 12, scale = 2)
    private BigDecimal cost;

    @Column(name = "service_center", length = 150)
    private String serviceCenter;
}
