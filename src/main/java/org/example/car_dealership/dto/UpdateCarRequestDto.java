package org.example.car_dealership.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Детальна інформація про автомобіль")
public class UpdateCarRequestDto {
    private String brand;
    private String model;
    private String manufacturer;
    private String registrationNumber;
    private BigDecimal engineVolume;
    private Integer enginePower;
    private BigDecimal fuelConsumption;
    private Integer doors;
    private Integer seats;
    private Integer trunkCapacity;
    private String transmission;
    private Boolean cruiseControl;
    private String fuelType;
    private Integer mileage;
    private LocalDate lastServiceDate;
    private BigDecimal price;
    private String color;
    private String interior;
    private String type;
    private Integer year;
    private List<String> imageUrls;
}
