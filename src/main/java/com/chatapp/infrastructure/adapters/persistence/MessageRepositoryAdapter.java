package com.chatapp.infrastructure.adapters.persistence;

import com.chatapp.domain.entities.Message;
import com.chatapp.domain.ports.MessageRepository;
import com.chatapp.infrastructure.adapters.persistence.mappers.EntityMapper;
import com.chatapp.infrastructure.adapters.persistence.repositories.JpaMessageRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageRepositoryAdapter implements MessageRepository {
  private final JpaMessageRepository jpaRepository;
  private final EntityMapper mapper;

  @Override
  public Message save(Message message) {
    return mapper.toDomain(jpaRepository.save(mapper.toEntity(message)));
  }

  @Override
  public List<Message> findByRoomId(UUID roomId) {
    return jpaRepository.findByRoomIdOrderBySentAtDesc(roomId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<Message> findByRoomIdWithLimit(UUID roomId, int limit) {
    return jpaRepository.findByRoomIdWithLimit(roomId, limit).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }
}
