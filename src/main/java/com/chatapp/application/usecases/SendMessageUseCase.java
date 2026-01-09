package com.chatapp.application.usecases;

import com.chatapp.domain.entities.Message;
import com.chatapp.domain.entities.User;
import com.chatapp.domain.events.MessageSentEvent;
import com.chatapp.domain.ports.MembershipRepository;
import com.chatapp.domain.ports.MessageRepository;
import com.chatapp.domain.ports.MessagingService;
import com.chatapp.domain.ports.RoomRepository;
import com.chatapp.domain.ports.UserRepository;
import com.chatapp.domain.services.MessageValidator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendMessageUseCase {
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final RoomRepository roomRepository;
  private final MembershipRepository membershipRepository;
  private final MessagingService messagingService;
  private final MessageValidator messageValidator = new MessageValidator();

  @Transactional
  public Message execute(UUID roomId, UUID userId, String content) {
    // Validate room exists
    roomRepository
        .findById(roomId)
        .orElseThrow(() -> new IllegalArgumentException("Room not found"));

    // Validate user exists
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    // Check membership
    if (!membershipRepository.existsByUserIdAndRoomId(userId, roomId)) {
      throw new IllegalArgumentException("User is not a member of this room");
    }

    // Validate message content
    messageValidator.validate(content);

    // Create and save message
    Message message = Message.create(roomId, userId, content);
    Message saved = messageRepository.save(message);

    // Publish message via messaging service
    messagingService.publishMessage(saved);

    // Publish domain event
    MessageSentEvent.of(saved);

    return saved;
  }
}
