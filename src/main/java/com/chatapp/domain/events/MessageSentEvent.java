package com.chatapp.domain.events;

import com.chatapp.domain.entities.Message;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSentEvent {
  private Message message;
  private Instant occurredAt;

  public static MessageSentEvent of(Message message) {
    return MessageSentEvent.builder().message(message).occurredAt(Instant.now()).build();
  }
}
