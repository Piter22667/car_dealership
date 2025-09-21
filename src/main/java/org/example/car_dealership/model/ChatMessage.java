package org.example.car_dealership.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.car_dealership.model.config.chat.SenderType;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderType sender;

    @Lob
    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}