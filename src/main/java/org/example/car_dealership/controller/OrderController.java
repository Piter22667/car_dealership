package org.example.car_dealership.controller;

import com.stripe.exception.StripeException;
import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.dto.StripeResponseDto;
import org.example.car_dealership.service.impl.OrderServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
        log.info("OrderController initialized - endpoints available at /orders/reserve, /orders/payment-success, /orders/payment-cancel");
    }


    @PostMapping(value = {"/reserve", "/reserve/"}) // Підтримка з і без trailing slash
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StripeResponseDto> processPayment(@RequestParam Long carId,
                                                            Authentication authentication) {
        log.info("=== ENDPOINT CALLED === Starting reservation process for carId={} by user={}", carId, authentication.getName());
        String userEmail = authentication.getName();

        StripeResponseDto stripeResponseDto = orderService.createOrderReservation(carId, userEmail);

        log.info("=== ENDPOINT SUCCESS === Stripe session created successfully: sessionId={}", stripeResponseDto.getSessionId());
        return ResponseEntity.ok(stripeResponseDto);
    }


    @GetMapping("/payment-success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestParam("session_id") String sessionId) {
        log.info("Processing successful payment for sessionId={}", sessionId);
        try {
            orderService.handleSuccessfulPayment(sessionId);
            log.info("Payment processed successfully for sessionId={}", sessionId);
            return ResponseEntity.ok("Оплата успішна! Авто заброньовано.");
        } catch (StripeException e) {
            log.error("Stripe error while processing payment for sessionId={}: {}", sessionId, e.getMessage());
            return ResponseEntity.badRequest().body("Помилка Stripe: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while processing payment for sessionId={}: {}", sessionId, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Помилка обробки оплати: " + e.getMessage());
        }
    }


    @GetMapping("/payment-cancel")
    public ResponseEntity<String> handlePaymentCancel(@RequestParam(required = false) String sessionId) {
        log.warn("Payment cancelled for sessionId={}", sessionId);
        return ResponseEntity.ok("Оплату скасовано. Резервацію не створено.");
    }

}
