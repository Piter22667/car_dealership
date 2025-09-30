package org.example.car_dealership.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.car_dealership.model.config.car.Type;
import java.math.BigDecimal;

@Data
@Schema(description = "Елемент списку автомобілів з основною інформацією")
public class CarListItemDto {
    private Long id;
    private String title;
    private Type type;
    private Integer year;
    private Integer mileage;
    private BigDecimal price;
    private String imageUrl;
}
