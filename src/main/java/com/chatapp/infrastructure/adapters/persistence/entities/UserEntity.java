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
    name = "users",
    indexes = {
      @Index(name = "idx_username", columnList = "username"),
      @Index(name = "idx_email", columnList = "email")
    })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id private UUID id;

  @Column(unique = true, nullable = false, length = 50)
  private String username;

  @Column(unique = true, nullable = false, length = 255)
  private String email;

  @Column(nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Instant updatedAt;
}
