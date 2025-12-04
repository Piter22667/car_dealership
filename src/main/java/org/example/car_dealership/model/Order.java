package org.example.car_dealership.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.car_dealership.model.config.orders.OrderStatus;
import org.example.car_dealership.model.config.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus currentStatus;

    private LocalDateTime lastChangedStatusAt;

    @Column(name = "requires_test_drive", nullable = false)
    private Boolean requiresTestDrive;

    /*
    Stripe logic
     */

    @Column(name = "stripe_payment_intend_id")
    private String paymentIntentId; // якщо замовлення успішно оплачено

    @Column(name = "stripe_session_id" )
    private String stripeSessionId; // id сесії для оплати

    @Column(name = "reservation_deposit", precision = 12, scale = 2)
    private BigDecimal reservationDeposit;

    @Column(name = "reservation_expires_at")
    private LocalDateTime reservationExpiresAt;

    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "is_deposit_refunded")
    private Boolean isDepositRefunded = false;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude // вказуємо для уникнення рекурсї між звязками та додаткового завантаження toString через анотації lombok
    @OneToMany(mappedBy = "order")
    private List<OrderStatusHistory> orderStatusHistory;
}