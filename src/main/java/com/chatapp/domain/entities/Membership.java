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
public class Membership {
  private UUID id;
  private UUID userId;
  private UUID roomId;
  private Instant joinedAt;

  public static Membership create(UUID userId, UUID roomId) {
    return Membership.builder()
        .id(UUID.randomUUID())
        .userId(userId)
        .roomId(roomId)
        .joinedAt(Instant.now())
        .build();
  }
}
