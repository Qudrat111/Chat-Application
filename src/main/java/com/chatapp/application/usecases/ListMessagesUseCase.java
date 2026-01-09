package com.chatapp.application.usecases;

import com.chatapp.domain.entities.Message;
import com.chatapp.domain.ports.MembershipRepository;
import com.chatapp.domain.ports.MessageRepository;
import com.chatapp.domain.ports.RoomRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListMessagesUseCase {
  private final MessageRepository messageRepository;
  private final RoomRepository roomRepository;
  private final MembershipRepository membershipRepository;

  @Transactional(readOnly = true)
  public List<Message> execute(UUID roomId, UUID userId, Integer limit) {
    // Validate room exists
    roomRepository
        .findById(roomId)
        .orElseThrow(() -> new IllegalArgumentException("Room not found"));

    // Check membership
    if (!membershipRepository.existsByUserIdAndRoomId(userId, roomId)) {
      throw new IllegalArgumentException("User is not a member of this room");
    }

    if (limit != null && limit > 0) {
      return messageRepository.findByRoomIdWithLimit(roomId, limit);
    }

    return messageRepository.findByRoomId(roomId);
  }
}
