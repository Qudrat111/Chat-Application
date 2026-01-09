package com.chatapp.application.usecases;

import com.chatapp.domain.entities.TypingIndicator;
import com.chatapp.domain.entities.User;
import com.chatapp.domain.ports.MembershipRepository;
import com.chatapp.domain.ports.MessagingService;
import com.chatapp.domain.ports.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishTypingUseCase {
  private final UserRepository userRepository;
  private final MembershipRepository membershipRepository;
  private final MessagingService messagingService;

  public void execute(UUID roomId, UUID userId, boolean typing) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    // Check membership
    if (!membershipRepository.existsByUserIdAndRoomId(userId, roomId)) {
      throw new IllegalArgumentException("User is not a member of this room");
    }

    TypingIndicator indicator =
        TypingIndicator.create(roomId, userId, user.getUsername(), typing);

    messagingService.publishTypingIndicator(indicator);
  }
}
