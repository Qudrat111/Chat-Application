package com.chatapp.domain.events;

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
public class UserJoinedRoomEvent {
  private UUID userId;
  private UUID roomId;
  private Instant occurredAt;

  public static UserJoinedRoomEvent of(UUID userId, UUID roomId) {
    return UserJoinedRoomEvent.builder()
        .userId(userId)
        .roomId(roomId)
        .occurredAt(Instant.now())
        .build();
  }
}
