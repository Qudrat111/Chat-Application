package com.chatapp.infrastructure.adapters.messaging;

import com.chatapp.domain.entities.Message;
import com.chatapp.domain.entities.TypingIndicator;
import com.chatapp.domain.ports.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessagingService implements MessagingService {
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void publishMessage(Message message) {
    String channel = "chat.room." + message.getRoomId();
    redisTemplate.convertAndSend(channel, message);
  }

  @Override
  public void publishTypingIndicator(TypingIndicator indicator) {
    String channel = "chat.room." + indicator.getRoomId() + ".typing";
    redisTemplate.convertAndSend(channel, indicator);
  }
}
