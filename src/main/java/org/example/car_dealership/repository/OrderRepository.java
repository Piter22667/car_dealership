package org.example.car_dealership.repository;

import org.example.car_dealership.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Перевіряє існування замовлення за Stripe session ID
     * Використовується для запобігання дублюванню замовлень при оновленні сторінки
     */
    boolean existsByStripeSessionId(String stripeSessionId);

    List<Order> findOrderByUserId(Long userId);

    @Query(value = "SELECT total_sales_value AS totalSalesValue, " +
            "average_car_price AS averageCarPrice, " +
            "available_cars_count AS availableCarsCount, " +
            "active_test_drives_count AS activeTestDrivesCount, " +
            "completed_orders_count AS completedOrdersCount " +
            "FROM get_dashboard_statistics()", nativeQuery = true)
    DashboardStatisticsProjection getDashboardStatistics();
}
