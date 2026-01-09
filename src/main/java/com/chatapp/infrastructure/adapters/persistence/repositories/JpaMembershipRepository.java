package com.chatapp.infrastructure.adapters.persistence.repositories;

import com.chatapp.infrastructure.adapters.persistence.entities.MembershipEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMembershipRepository extends JpaRepository<MembershipEntity, UUID> {
  Optional<MembershipEntity> findByUserIdAndRoomId(UUID userId, UUID roomId);

  List<MembershipEntity> findByRoomId(UUID roomId);

  List<MembershipEntity> findByUserId(UUID userId);

  boolean existsByUserIdAndRoomId(UUID userId, UUID roomId);
}
