package org.example.car_dealership.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarImageResponseDto {
//    private Integer id;
    private String storageKey;
    private String url;
    private boolean isPrimary;
}
