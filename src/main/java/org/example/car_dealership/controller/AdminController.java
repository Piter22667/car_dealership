package org.example.car_dealership.controller;

import com.stripe.exception.StripeException;
import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.dto.DashboardStatisticsDto;
import org.example.car_dealership.dto.OrderAdminDto;
import org.example.car_dealership.dto.OrderResponseDto;
import org.example.car_dealership.dto.TestDriveAdminDto;
import org.example.car_dealership.service.OrderService;
import org.example.car_dealership.service.TestDriveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final OrderService orderService;
    private final TestDriveService testDriveService;

    public AdminController(OrderService orderService, TestDriveService testDriveService) {
        this.orderService = orderService;
        this.testDriveService = testDriveService;
        log.info("AdminController initialized - admin endpoints available at /admin/*");
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderAdminDto>> getAllOrders() {
        log.info("Admin fetching all orders");
        List<OrderAdminDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderAdminDto> getOrderById(@PathVariable Long orderId) {
        log.info("Admin fetching order: orderId={}", orderId);
        OrderAdminDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/orders/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId, Authentication authentication) {
        log.info("Admin canceling order: orderId={}, admin={}", orderId, authentication.getName());
        try {
            OrderAdminDto canceledOrder = orderService.adminCancelOrder(orderId, authentication.getName());
            log.info("Order canceled successfully: orderId={}", orderId);
            return ResponseEntity.ok(canceledOrder);
        } catch (StripeException e) {
            log.error("Stripe error while canceling order: orderId={}, error={}", orderId, e.getMessage());
            return ResponseEntity.badRequest().body("Помилка Stripe при поверненні депозиту: " + e.getMessage());
        } catch (IllegalStateException e) {
            log.warn("Invalid state for order cancellation: orderId={}, error={}", orderId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while canceling order: orderId={}", orderId, e);
            return ResponseEntity.badRequest().body("Помилка при скасуванні замовлення: " + e.getMessage());
        }
    }

    @PostMapping("/orders/{orderId}/confirm")
    public ResponseEntity<?> confirmOrder(@PathVariable Long orderId) {
        log.info("Admin confirming order: orderId={}", orderId);
        try {
            OrderResponseDto confirmedOrder = orderService.confirmOrder(orderId);
            log.info("Order confirmed successfully: orderId={}", orderId);
            return ResponseEntity.ok(confirmedOrder);
        } catch (IllegalStateException e) {
            log.warn("Invalid state for order confirmation: orderId={}, error={}", orderId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error confirming order: orderId={}", orderId, e);
            return ResponseEntity.badRequest().body("Помилка при підтвердженні замовлення: " + e.getMessage());
        }
    }

    @GetMapping("/dashboard/general")
    public ResponseEntity<DashboardStatisticsDto> getDashboardStatistics() {
        log.info("Admin fetching dashboard statistics");
        DashboardStatisticsDto statistics = orderService.getDashboardStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/test-drives")
    public ResponseEntity<List<TestDriveAdminDto>> getAllTestDrives() {
        log.info("Admin fetching all test drives");
        List<TestDriveAdminDto> testDrives = testDriveService.getAllTestDrives();
        return ResponseEntity.ok(testDrives);
    }

    @PostMapping("/test-drives/{testDriveId}/approve")
    public ResponseEntity<?> approveTestDrive(@PathVariable Long testDriveId, Authentication authentication) {
        log.info("Admin approving test drive: testDriveId={}, admin={}", testDriveId, authentication.getName());
        try {
            TestDriveAdminDto testDrive = testDriveService.approveTestDrive(testDriveId, authentication.getName());
            log.info("Test drive approved successfully: testDriveId={}", testDriveId);
            return ResponseEntity.ok(testDrive);
        } catch (IllegalStateException e) {
            log.warn("Invalid state for test drive approval: testDriveId={}, error={}", testDriveId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error approving test drive: testDriveId={}", testDriveId, e);
            return ResponseEntity.badRequest().body("Помилка при схваленні тест-драйву: " + e.getMessage());
        }
    }

    @PostMapping("/test-drives/{testDriveId}/cancel")
    public ResponseEntity<?> cancelTestDrive(@PathVariable Long testDriveId, Authentication authentication) {
        log.info("Admin canceling test drive: testDriveId={}, admin={}", testDriveId, authentication.getName());
        try {
            TestDriveAdminDto testDrive = testDriveService.cancelTestDrive(testDriveId, authentication.getName());
            log.info("Test drive canceled successfully: testDriveId={}", testDriveId);
            return ResponseEntity.ok(testDrive);
        } catch (IllegalStateException e) {
            log.warn("Invalid state for test drive cancellation: testDriveId={}, error={}", testDriveId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error canceling test drive: testDriveId={}", testDriveId, e);
            return ResponseEntity.badRequest().body("Помилка при скасуванні тест-драйву: " + e.getMessage());
        }
    }

    @PostMapping("/test-drives/{testDriveId}/complete")
    public ResponseEntity<?> completeTestDrive(@PathVariable Long testDriveId, Authentication authentication) {
        log.info("Admin completing test drive: testDriveId={}, admin={}", testDriveId, authentication.getName());
        try {
            TestDriveAdminDto testDrive = testDriveService.completeTestDrive(testDriveId, authentication.getName());
            log.info("Test drive completed successfully: testDriveId={}", testDriveId);
            return ResponseEntity.ok(testDrive);
        } catch (IllegalStateException e) {
            log.warn("Invalid state for test drive completion: testDriveId={}, error={}", testDriveId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error completing test drive: testDriveId={}", testDriveId, e);
            return ResponseEntity.badRequest().body("Помилка при завершенні тест-драйву: " + e.getMessage());
        }
    }
}

