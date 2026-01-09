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
public class Room {
  private UUID id;
  private String name;
  private String description;
  private boolean isPrivate;
  private UUID createdBy;
  private Instant createdAt;
  private Instant updatedAt;

  public static Room create(String name, String description, boolean isPrivate, UUID createdBy) {
    return Room.builder()
        .id(UUID.randomUUID())
        .name(name)
        .description(description)
        .isPrivate(isPrivate)
        .createdBy(createdBy)
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();
  }
}
