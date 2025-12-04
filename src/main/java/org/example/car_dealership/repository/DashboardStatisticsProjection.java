package org.example.car_dealership.repository;

import java.math.BigDecimal;

public interface DashboardStatisticsProjection {

    BigDecimal getTotalSalesValue();

    BigDecimal getAverageCarPrice();

    Long getAvailableCarsCount();

    Long getActiveTestDrivesCount();

    Long getCompletedOrdersCount();
}
