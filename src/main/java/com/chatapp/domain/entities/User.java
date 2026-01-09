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
public class User {
  private UUID id;
  private String username;
  private String email;
  private String passwordHash;
  private Instant createdAt;
  private Instant updatedAt;

  public static User create(String username, String email, String passwordHash) {
    return User.builder()
        .id(UUID.randomUUID())
        .username(username)
        .email(email)
        .passwordHash(passwordHash)
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();
  }
}
