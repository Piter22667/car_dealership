package org.example.car_dealership.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.car_dealership.model.config.car.FuelType;
import org.example.car_dealership.model.config.car.Interior;
import org.example.car_dealership.model.config.car.Transmission;
import org.example.car_dealership.model.config.car.Type;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Дто для отримання інформації від клієнта про створення автомобіля")
public class CreateCarRequestDto {
    @NotBlank(message = "Brand is required")
    @Size(max = 100, message = "Brand must not exceed 100 characters")
    private String brand;

    @NotBlank(message = "Model is required")
    @Size(max = 100, message = "Model must not exceed 100 characters")
    private String model;

    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    private String manufacturer;

    @Size(max = 50, message = "Registration number must not exceed 50 characters")
    private String registrationNumber;

    @DecimalMin(value = "0.1", message = "Engine volume must be positive")
    @DecimalMax(value = "99.9", message = "Engine volume must not exceed 99.9")
    private BigDecimal engineVolume;

    @Min(value = 1, message = "Engine power must be positive")
    private Integer enginePower;

    @DecimalMin(value = "0.0", message = "Fuel consumption must be non-negative")
    private BigDecimal fuelConsumption;

    @Min(value = 2, message = "Doors must be at least 2")
    @Max(value = 5, message = "Doors must not exceed 5")
    private Integer doors;

    @Min(value = 1, message = "Seats must be at least 1")
    @Max(value = 9, message = "Seats must not exceed 9")
    private Integer seats;

    @Min(value = 0, message = "Trunk capacity must be non-negative")
    private Integer trunkCapacity;

    @NotNull(message = "Transmission is required")
    private Transmission transmission;

    private Boolean cruiseControl;

    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;

    @Min(value = 0, message = "Mileage must be non-negative")
    private Integer mileage;

    private LocalDate lastServiceDate;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be non-negative")
    private BigDecimal price;

    @Size(max = 50, message = "Color must not exceed 50 characters")
    private String color;

    private Interior interior;

    @NotNull(message = "Type is required")
    private Type type;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be at least 1900")
    @Max(value = 2100, message = "Year must not exceed 2100")
    private Integer year;
}