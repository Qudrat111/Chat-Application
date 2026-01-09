package com.chatapp.infrastructure.adapters.persistence.repositories;

import com.chatapp.infrastructure.adapters.persistence.entities.RoomEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRoomRepository extends JpaRepository<RoomEntity, UUID> {
  List<RoomEntity> findByIsPrivateFalse();

  @Query(
      "SELECT r FROM RoomEntity r JOIN MembershipEntity m ON r.id = m.roomId WHERE m.userId ="
          + " :userId")
  List<RoomEntity> findByUserId(@Param("userId") UUID userId);
}
