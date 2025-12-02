package org.example.car_dealership.repository;

import org.example.car_dealership.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Перевіряє існування замовлення за Stripe session ID
     * Використовується для запобігання дублюванню замовлень при оновленні сторінки
     */
    boolean existsByStripeSessionId(String stripeSessionId);
}
