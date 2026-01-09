package com.chatapp.domain.ports;

import com.chatapp.domain.entities.Message;
import com.chatapp.domain.entities.TypingIndicator;

public interface MessagingService {
  void publishMessage(Message message);

  void publishTypingIndicator(TypingIndicator indicator);
}
