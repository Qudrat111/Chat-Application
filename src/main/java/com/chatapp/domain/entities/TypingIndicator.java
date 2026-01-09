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
public class TypingIndicator {
  private UUID roomId;
  private UUID userId;
  private String username;
  private boolean typing;
  private Instant timestamp;

  public static TypingIndicator create(UUID roomId, UUID userId, String username, boolean typing) {
    return TypingIndicator.builder()
        .roomId(roomId)
        .userId(userId)
        .username(username)
        .typing(typing)
        .timestamp(Instant.now())
        .build();
  }
}
