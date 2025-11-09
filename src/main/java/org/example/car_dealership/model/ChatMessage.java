package org.example.car_dealership.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import org.example.car_dealership.model.config.chat.SenderType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    private ChatThread thread;


    @ManyToOne(optional = true)
    @JoinColumn(name = "sender_user_id")
    private User senderUser; // безпосередньо юзер (має id), який надіслав повідомлення (або ші -> null)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderType sender; // тип "акаунту" користувача, який надіслав повідомлення

    @Lob
    @Column(name = "message_text", nullable = false)
    private String messageText;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Lob
    @Column(name = "ai_response_metadata", nullable = true)
    private String aiResponseMetadata;

}