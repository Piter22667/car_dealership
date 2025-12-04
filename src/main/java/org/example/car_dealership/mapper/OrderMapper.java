package org.example.car_dealership.mapper;

import org.example.car_dealership.dto.OrderAdminDto;
import org.example.car_dealership.dto.OrderForUserDto;
import org.example.car_dealership.dto.OrderResponseDto;
import org.example.car_dealership.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    public List<OrderForUserDto> toOrderForUserDtoList(List<Order> orders) {
        return orders.stream()
                .map(order -> OrderForUserDto.builder()
                        .id(order.getId())
                        .currentStatus(order.getCurrentStatus())
                        .orderDate(order.getOrderDate())
                        .totalPrice(order.getTotalPrice())
                        .reservationDeposit(order.getReservationDeposit())
                        .reservationExpiresAt(order.getReservationExpiresAt())
                        .paymentStatus(order.getPaymentStatus())
                        .paymentDate(order.getPaymentDate())
                        .requiresTestDrive(order.getRequiresTestDrive())
                        .isDepositRefunded(order.getIsDepositRefunded())
                        .userId(order.getUser().getId())
                        .car(new OrderForUserDto.CarDto(
                                order.getCar().getId(),
                                order.getCar().getBrand(),
                                order.getCar().getModel(),
                                order.getCar().getYear(),
                                order.getCar().getPrice()
                        ))
                        .build())
                .toList();
    }

    public OrderAdminDto toOrderAdminDto(Order order) {
        return OrderAdminDto.builder()
                .id(order.getId())
                .currentStatus(order.getCurrentStatus())
                .orderDate(order.getOrderDate())
                .lastStatusChangedAt(order.getLastChangedStatusAt())
                .totalPrice(order.getTotalPrice())
                .reservationDeposit(order.getReservationDeposit())
                .reservationExpiresAt(order.getReservationExpiresAt())
                .paymentStatus(order.getPaymentStatus())
                .paymentDate(order.getPaymentDate())
                .requiresTestDrive(order.getRequiresTestDrive())
                .isDepositRefunded(order.getIsDepositRefunded())
                .stripePaymentIntentId(order.getPaymentIntentId())
                .user(new OrderAdminDto.UserDto(
                        order.getUser().getId(),
                        order.getUser().getEmail(),
                        order.getUser().getName(),
                        order.getUser().getPhoneNumber()
                ))
                .car(new OrderAdminDto.CarDto(
                        order.getCar().getId(),
                        order.getCar().getBrand(),
                        order.getCar().getModel(),
                        order.getCar().getYear(),
                        order.getCar().getPrice()
                ))
                .build();
    }

    public List<OrderAdminDto> toOrderAdminDtoList(List<Order> orders) {
        return orders.stream()
                .map(this::toOrderAdminDto)
                .toList();
    }

    public OrderResponseDto toOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .currentStatus(order.getCurrentStatus())
                .orderDate(order.getOrderDate())
                .lastStatusChangedAt(order.getLastChangedStatusAt())
                .totalPrice(order.getTotalPrice())
                .reservationDeposit(order.getReservationDeposit())
                .reservationExpiresAt(order.getReservationExpiresAt())
                .paymentStatus(order.getPaymentStatus())
                .paymentDate(order.getPaymentDate())
                .requiresTestDrive(order.getRequiresTestDrive())
                .isDepositRefunded(order.getIsDepositRefunded())
                .car(new OrderResponseDto.CarDto(
                        order.getCar().getId(),
                        order.getCar().getBrand(),
                        order.getCar().getModel(),
                        order.getCar().getYear(),
                        order.getCar().getPrice(),
                        order.getCar().getStatus()
                ))
                .user(new OrderResponseDto.UserDto(
                        order.getUser().getId(),
                        order.getUser().getEmail(),
                        order.getUser().getName()
                ))
                .build();
    }
}
