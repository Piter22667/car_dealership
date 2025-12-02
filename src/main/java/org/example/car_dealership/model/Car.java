package org.example.car_dealership.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.car_dealership.model.config.car.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String brand;

    @Enumerated(EnumType.STRING)
    private Type type;

    private Integer year;

    @Column(length = 100)
    private String model;

    @Column(length = 100)
    private String manufacturer;

    @Column(name = "registration_number", length = 50, unique = true)
    private String registrationNumber;

    @Column(name = "engine_volume", precision = 4, scale = 1)
    private BigDecimal engineVolume;

    @Column(name = "engine_power")
    private Integer enginePower;

    @Column(name = "fuel_consumption", precision = 4, scale = 1)
    private BigDecimal fuelConsumption;

    private Integer doors;

    private Integer seats;

    @Column(name = "trunk_capacity")
    private Integer trunkCapacity;

    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    @Column(name = "cruise_control")
    private Boolean cruiseControl;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type")
    private FuelType fuelType;

    private Integer mileage;

    @Column(name = "last_service_date")
    private LocalDate lastServiceDate;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column(length = 50)
    private String color;

    @Enumerated(EnumType.STRING)
    private Interior interior;

    @Version
    @Builder.Default
    private Long version = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private CarStatus status = CarStatus.AVAILABLE;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    // вказуємо для уникнення рекурсї між звязками та додаткового завантаження toString через анотації lombok
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CarImage> carImages = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    // вказуємо для уникнення рекурсї між звязками та додаткового завантаження toString через анотації lombok
    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<ServiceHistory> serviceHistories = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    // вказуємо для уникнення рекурсї між звязками та додаткового завантаження toString через анотації lombok
    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<TestDrive> testDrives = new ArrayList<>();

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

}