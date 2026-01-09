package com.chatapp.domain.ports;

import com.chatapp.domain.entities.Membership;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipRepository {
  Membership save(Membership membership);

  Optional<Membership> findByUserIdAndRoomId(UUID userId, UUID roomId);

  List<Membership> findByRoomId(UUID roomId);

  List<Membership> findByUserId(UUID userId);

  void delete(Membership membership);

  boolean existsByUserIdAndRoomId(UUID userId, UUID roomId);
}
