package com.chatapp.infrastructure.adapters.persistence.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "messages",
    indexes = {
      @Index(name = "idx_room_id_sent_at", columnList = "room_id,sent_at"),
      @Index(name = "idx_sender_id", columnList = "sender_id")
    })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
  @Id private UUID id;

  @Column(nullable = false)
  private UUID roomId;

  @Column(nullable = false)
  private UUID senderId;

  @Column(nullable = false, length = 4000)
  private String content;

  @Column(nullable = false)
  private Instant sentAt;
}
