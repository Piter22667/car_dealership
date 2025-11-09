package org.example.car_dealership.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chat_threads")
@Data
public class ChatThread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Optional linkage to a business context
    @ManyToOne(optional = true)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(optional = true)
    @JoinColumn(name = "test_drive_id")
    private TestDrive testDrive;

    @Column(length = 150)
    private String title;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude // вказуємо для уникнення рекурсї між звязками та додаткового завантаження toString через анотації lombok
    @OneToMany(mappedBy = "thread", orphanRemoval = true)
    private List<ChatMessage> messages;

}