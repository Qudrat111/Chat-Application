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
    name = "memberships",
    indexes = {
      @Index(name = "idx_user_id", columnList = "user_id"),
      @Index(name = "idx_room_id", columnList = "room_id"),
      @Index(name = "idx_user_room", columnList = "user_id,room_id")
    })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipEntity {
  @Id private UUID id;

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false)
  private UUID roomId;

  @Column(nullable = false)
  private Instant joinedAt;
}
