package com.chatapp.infrastructure.adapters.persistence.repositories;

import com.chatapp.infrastructure.adapters.persistence.entities.MessageEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMessageRepository extends JpaRepository<MessageEntity, UUID> {
  List<MessageEntity> findByRoomIdOrderBySentAtDesc(UUID roomId);

  default List<MessageEntity> findByRoomIdWithLimit(UUID roomId, int limit) {
    Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "sentAt"));
    return findByRoomId(roomId, pageable);
  }

  List<MessageEntity> findByRoomId(UUID roomId, Pageable pageable);
}
