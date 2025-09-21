package org.example.car_dealership.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.car_dealership.model.config.user.Role;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;
    private String address;

    @Column(nullable = false)
    private String password_hash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TestDrive> testDrives;

    @OneToMany(mappedBy = "userWhoChangedTestDriveStatus", cascade = CascadeType.ALL)
    private List<TestDriveStatusHistory> testDriveStatusHistories;

    @OneToMany(mappedBy = "userWhoChangedOrderStatus", cascade = CascadeType.ALL)
    private List<OrderStatusHistory> orderStatusHistories;

}
