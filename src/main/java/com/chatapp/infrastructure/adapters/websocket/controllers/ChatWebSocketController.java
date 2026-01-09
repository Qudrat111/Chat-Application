package com.chatapp.infrastructure.adapters.websocket.controllers;

import com.chatapp.application.dto.MessageResponse;
import com.chatapp.application.dto.SendMessageRequest;
import com.chatapp.application.dto.TypingIndicatorMessage;
import com.chatapp.application.usecases.PublishTypingUseCase;
import com.chatapp.application.usecases.SendMessageUseCase;
import com.chatapp.domain.entities.Message;
import com.chatapp.domain.entities.User;
import com.chatapp.domain.ports.UserRepository;
import com.chatapp.infrastructure.security.services.UserDetailsServiceImpl;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
  private final SendMessageUseCase sendMessageUseCase;
  private final PublishTypingUseCase publishTypingUseCase;
  private final UserDetailsServiceImpl userDetailsService;
  private final UserRepository userRepository;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/rooms/{roomId}/messages")
  public void sendMessage(
      @DestinationVariable UUID roomId,
      @Payload SendMessageRequest request,
      Principal principal) {
    UUID userId = userDetailsService.getUserIdByUsername(principal.getName());
    Message message = sendMessageUseCase.execute(roomId, userId, request.getContent());

    // Get sender username
    String senderUsername =
        userRepository.findById(userId).map(User::getUsername).orElse("Unknown");

    // Broadcast to room
    MessageResponse response =
        MessageResponse.builder()
            .id(message.getId())
            .roomId(message.getRoomId())
            .senderId(message.getSenderId())
            .senderUsername(senderUsername)
            .content(message.getContent())
            .sentAt(message.getSentAt())
            .build();

    messagingTemplate.convertAndSend("/topic/rooms/" + roomId, response);
  }

  @MessageMapping("/rooms/{roomId}/typing")
  public void publishTyping(
      @DestinationVariable UUID roomId,
      @Payload TypingIndicatorMessage request,
      Principal principal) {
    UUID userId = userDetailsService.getUserIdByUsername(principal.getName());
    publishTypingUseCase.execute(roomId, userId, request.isTyping());

    // Broadcast typing indicator
    messagingTemplate.convertAndSend("/topic/rooms/" + roomId + "/typing", request);
  }
}
