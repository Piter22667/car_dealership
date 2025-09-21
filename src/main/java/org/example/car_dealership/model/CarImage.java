package org.example.car_dealership.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "car_images")
@Data
public class CarImage {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "image_url", length = 2048, nullable = false)
    private String imageUrl;
}
