package com.chatapp.infrastructure.adapters.persistence.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms", indexes = {@Index(name = "idx_created_by", columnList = "created_by")})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomEntity {
  @Id private UUID id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  @Column(nullable = false)
  private boolean isPrivate;

  @Column(nullable = false)
  private UUID createdBy;

  @Column(nullable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Instant updatedAt;
}
