package org.example.car_dealership.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.car_dealership.model.config.car.ImageVariant;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "car_images")
@Data
public class CarImage {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

//    @Column(name = "image_url", length = 2048, nullable = false)
//    private String imageUrl;

    @Column(name = "storage_key", nullable = false, length = 1024)
    private String storageKey;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_variant", nullable = false)
    private ImageVariant imageVariant;


    @CreationTimestamp
    private LocalDateTime createdAt;
}
