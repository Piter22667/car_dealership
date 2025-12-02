package org.example.car_dealership.service;

import com.stripe.exception.StripeException;
import org.example.car_dealership.dto.OrderForUserDto;
import org.example.car_dealership.dto.StripeResponseDto;

import java.util.List;

public interface OrderService {
    StripeResponseDto createOrderReservation(Long carId, String userEmail);

    void handleSuccessfulPayment(String sessionId) throws StripeException;

    List<OrderForUserDto> getOrdersForUser(String userEmail);
}
