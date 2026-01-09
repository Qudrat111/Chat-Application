package com.chatapp.application.usecases;

import com.chatapp.domain.entities.Membership;
import com.chatapp.domain.events.UserLeftRoomEvent;
import com.chatapp.domain.ports.MembershipRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveRoomUseCase {
  private final MembershipRepository membershipRepository;

  @Transactional
  public void execute(UUID roomId, UUID userId) {
    Membership membership =
        membershipRepository
            .findByUserIdAndRoomId(userId, roomId)
            .orElseThrow(() -> new IllegalArgumentException("Membership not found"));

    membershipRepository.delete(membership);

    // Publish event (simplified - would use event publisher in production)
    UserLeftRoomEvent.of(userId, roomId);
  }
}
