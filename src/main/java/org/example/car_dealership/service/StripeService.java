package org.example.car_dealership.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.dto.StripeResponseDto;
import org.example.car_dealership.exception.CarNotExistException;
import org.example.car_dealership.model.Car;
import org.example.car_dealership.repository.CarRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StripeService {

    private final CarRepository carRepository;

    @Value("${stripe.secret.key}")
    private String secretKey;

    public StripeService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    //request
    // productName, price, quantity, currency  -->  Stripe
    // --> return sessionId, sessionUrl

    public StripeResponseDto createPaymentSession(Long carId, Long userId) {
        log.info("Creating Stripe payment session for carId={}, userId={}", carId, userId);

        Stripe.apiKey = secretKey;

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> {
                    log.error("Car not found when creating Stripe session: carId={}", carId);
                    return new CarNotExistException("Car not found");
                });

        log.info("Creating session for car: {} {}, price: ${}", car.getBrand(), car.getModel(), car.getPrice());

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Депозит для бронювання " + car.getBrand() + " " + car.getModel())
                        .setDescription("Резервація автомобіля на 7 днів")
                        .build();


        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setProductData(productData)
                .setUnitAmount(10000L)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPriceData(priceData)
                .setQuantity(1L)
                .build();

        SessionCreateParams stripeParameters = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/payment-success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8080/payment-cancel.html")
                .addLineItem(lineItem)
                .putMetadata("carId", String.valueOf(carId))
                .putMetadata("userId", String.valueOf(userId))
                .setExpiresAt(System.currentTimeMillis() / 1000 + 1800) // 30 хвилин
                .build();

        Session session;
        try {
            session = Session.create(stripeParameters);
            log.info("Stripe session created successfully: sessionId={}, sessionUrl={}", session.getId(), session.getUrl());
        } catch (StripeException e) {
            log.error("Failed to create Stripe session for carId={}, userId={}: {}", carId, userId, e.getMessage(), e);
            throw new RuntimeException("Помилка створення сесії Stripe: " + e.getMessage());
        }

        return StripeResponseDto.builder()
                .status("SUCCESS")
                .message("Сесія оплати створена")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }

    public Session retrieveSession(String sessionId) throws StripeException {
        log.info("Retrieving Stripe session: sessionId={}", sessionId);
        Stripe.apiKey = secretKey;
        Session session = Session.retrieve(sessionId);
        log.info("Session retrieved successfully: sessionId={}, paymentStatus={}", sessionId, session.getPaymentStatus());
        return session;
    }

}
