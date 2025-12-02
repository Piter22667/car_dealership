package org.example.car_dealership.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.car_dealership.model.config.user.Role;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;
    private String address;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;


    @OneToMany(mappedBy = "senderUser")
    private List<ChatMessage> chatMessages;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude // вказуємо для уникнення рекурсї між звязками та додаткового завантаження toString через анотації lombok
    @OneToMany(mappedBy = "user")
    private List<TestDrive> testDrives;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude // вказуємо для уникнення рекурсї між звязками та додаткового завантаження toString через анотації lombok
    @OneToMany(mappedBy = "userWhoChangedTestDriveStatus")
    private List<TestDriveStatusHistory> testDriveStatusHistories;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude // вказуємо для уникнення рекурсї між звязками та додаткового завантаження toString через анотації lombok
    @OneToMany(mappedBy = "userWhoChangedOrderStatus")
    private List<OrderStatusHistory> orderStatusHistories;


    //Userdetails interface implementation

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    } //використовуємо email як username для аутентифікації

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
