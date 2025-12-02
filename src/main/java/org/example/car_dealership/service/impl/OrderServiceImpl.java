package org.example.car_dealership.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.dto.StripeResponseDto;
import org.example.car_dealership.exception.CarNotAvailableException;
import org.example.car_dealership.exception.CarNotExistException;
import org.example.car_dealership.model.Car;
import org.example.car_dealership.model.Order;
import org.example.car_dealership.model.OrderStatusHistory;
import org.example.car_dealership.model.User;
import org.example.car_dealership.model.config.car.CarStatus;
import org.example.car_dealership.model.config.orders.OrderStatus;
import org.example.car_dealership.model.config.payment.PaymentStatus;
import org.example.car_dealership.repository.CarRepository;
import org.example.car_dealership.repository.OrderRepository;
import org.example.car_dealership.repository.OrderStatusHistoryRepository;
import org.example.car_dealership.repository.UserRepository;
import org.example.car_dealership.service.StripeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final CarRepository carRepository;
    private final StripeService stripeService;
    private final UserRepository userRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CarRepository carRepository, StripeService stripeService, UserRepository userRepository, OrderStatusHistoryRepository orderStatusHistoryRepository) {
        this.orderRepository = orderRepository;
        this.carRepository = carRepository;
        this.stripeService = stripeService;
        this.userRepository = userRepository;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
    }

    public StripeResponseDto createOrderReservation(Long carId, String userEmail) {
        log.info("Creating reservation for carId={}, userEmail={}", carId, userEmail);

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> {
                    log.error("Car not found: carId={}", carId);
                    return new CarNotExistException("Car not found");
                });

        if (car.getStatus() != CarStatus.AVAILABLE) {
            log.warn("Car is not available for reservation: carId={}, currentStatus={}", carId, car.getStatus());
            throw new CarNotAvailableException("Car is not available for reservation");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.error("User not found: email={}", userEmail);
                    return new RuntimeException("User not found");
                });

        car.setStatus(CarStatus.RESERVED_PENDING);
        carRepository.save(car);
        log.info("Car status changed to RESERVED_PENDING: carId={}", carId);

        StripeResponseDto response = stripeService.createPaymentSession(car.getId(), user.getId());
        log.info("Reservation created successfully for carId={}, userId={}, sessionId={}", carId, user.getId(), response.getSessionId());

        return response;
    }


    @Transactional
    public void handleSuccessfulPayment(String sessionId) throws StripeException {
        log.info("Handling successful payment for sessionId={}", sessionId);

        // Перевірка на дублікат замовлення
        if (orderRepository.existsByStripeSessionId(sessionId)) {
            log.warn("Order already exists for sessionId={}. Skipping duplicate creation.", sessionId);
            return;
        }

        Session session = stripeService.retrieveSession(sessionId);

        Long carId = Long.valueOf(session.getMetadata().get("carId"));
        Long userId = Long.valueOf(session.getMetadata().get("userId"));

        log.info("Retrieved session metadata: carId={}, userId={}", carId, userId);

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> {
                    log.error("Car not found during payment processing: carId={}", carId);
                    return new CarNotExistException("Car not found");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found during payment processing: userId={}", userId);
                    return new RuntimeException("User not found");
                });

        Order order = new Order();
        order.setCar(car);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(car.getPrice());
        order.setPaymentIntentId(session.getPaymentIntent());
        order.setStripeSessionId(sessionId);
        order.setReservationDeposit(new BigDecimal("100.00"));
        order.setReservationExpiresAt(LocalDateTime.now().plusDays(7));
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setPaymentDate(LocalDateTime.now());
        order.setIsDepositRefunded(false);
        order.setCurrentStatus(OrderStatus.PROCESSING);
        order.setRequiresTestDrive(false);

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully: orderId={}, carId={}, userId={}", savedOrder.getId(), carId, userId);

        // Створюємо запис в історію статусів замовлення
        createOrderStatusHistory(savedOrder, OrderStatus.PROCESSING, user);

        car.setStatus(CarStatus.RESERVED);
        carRepository.save(car);
        log.info("Car status updated to RESERVED: carId={}", carId);
    }

    private void createOrderStatusHistory(Order order, OrderStatus status, User user) {
        OrderStatusHistory statusHistory = new OrderStatusHistory();
        statusHistory.setOrder(order);
        statusHistory.setUserWhoChangedOrderStatus(user);
        statusHistory.setStatus(status);
        statusHistory.setChangedAt(LocalDateTime.now());

        orderStatusHistoryRepository.save(statusHistory);
        log.info("Order status history created: orderId={}, status={}, changedBy={}",
                 order.getId(), status, user.getEmail());
    }
}
