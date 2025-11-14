package org.example.car_dealership.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.car_dealership.model.config.car.Type;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarFilterDto {
    private String brand;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Type type;
}
