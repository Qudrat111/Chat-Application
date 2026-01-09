package com.chatapp.domain.entities;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
  private UUID id;
  private UUID roomId;
  private UUID senderId;
  private String content;
  private Instant sentAt;

  public static Message create(UUID roomId, UUID senderId, String content) {
    return Message.builder()
        .id(UUID.randomUUID())
        .roomId(roomId)
        .senderId(senderId)
        .content(content)
        .sentAt(Instant.now())
        .build();
  }
}
