package org.example.car_dealership.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.car_dealership.model.config.orders.OrderStatus;
import org.example.car_dealership.model.config.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderForUserDto {
    private Long id;
    private OrderStatus currentStatus;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private BigDecimal reservationDeposit;
    private LocalDateTime reservationExpiresAt;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private Boolean requiresTestDrive;
    private Boolean isDepositRefunded;
    private Long userId;
    private CarDto car;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CarDto {
        private Long carId;
        private String brand;
        private String model;
        private Integer year;
        private BigDecimal price;
    }
}
