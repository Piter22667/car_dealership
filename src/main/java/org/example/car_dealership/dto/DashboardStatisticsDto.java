package org.example.car_dealership.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatisticsDto {
    private BigDecimal totalSalesValue;
    private BigDecimal averageCarPrice;
    private Long availableCarsCount;
    private Long activeTestDrivesCount;
    private Long completedOrdersCount;
}
