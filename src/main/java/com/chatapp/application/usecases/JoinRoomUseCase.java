package com.chatapp.application.usecases;

import com.chatapp.domain.entities.Membership;
import com.chatapp.domain.entities.Room;
import com.chatapp.domain.entities.User;
import com.chatapp.domain.events.UserJoinedRoomEvent;
import com.chatapp.domain.ports.MembershipRepository;
import com.chatapp.domain.ports.RoomRepository;
import com.chatapp.domain.ports.UserRepository;
import com.chatapp.domain.services.RoomAccessPolicy;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinRoomUseCase {
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;
  private final MembershipRepository membershipRepository;
  private final RoomAccessPolicy roomAccessPolicy = new RoomAccessPolicy();

  @Transactional
  public Membership execute(UUID roomId, UUID userId) {
    Room room =
        roomRepository
            .findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found"));

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    // Check if user can join room
    if (!roomAccessPolicy.canJoinRoom(room, user)) {
      throw new IllegalArgumentException("User cannot join this private room");
    }

    // Check if already a member
    if (membershipRepository.existsByUserIdAndRoomId(userId, roomId)) {
      throw new IllegalArgumentException("User is already a member of this room");
    }

    Membership membership = Membership.create(userId, roomId);
    Membership saved = membershipRepository.save(membership);

    // Publish event (simplified - would use event publisher in production)
    UserJoinedRoomEvent.of(userId, roomId);

    return saved;
  }
}
