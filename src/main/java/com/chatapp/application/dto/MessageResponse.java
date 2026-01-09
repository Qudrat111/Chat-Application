package com.chatapp.application.dto;

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
public class MessageResponse {
  private UUID id;
  private UUID roomId;
  private UUID senderId;
  private String senderUsername;
  private String content;
  private Instant sentAt;
}
