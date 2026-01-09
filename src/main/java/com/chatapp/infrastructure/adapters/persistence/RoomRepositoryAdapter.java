package com.chatapp.infrastructure.adapters.persistence;

import com.chatapp.domain.entities.Room;
import com.chatapp.domain.ports.RoomRepository;
import com.chatapp.infrastructure.adapters.persistence.mappers.EntityMapper;
import com.chatapp.infrastructure.adapters.persistence.repositories.JpaRoomRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomRepositoryAdapter implements RoomRepository {
  private final JpaRoomRepository jpaRepository;
  private final EntityMapper mapper;

  @Override
  public Room save(Room room) {
    return mapper.toDomain(jpaRepository.save(mapper.toEntity(room)));
  }

  @Override
  public Optional<Room> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<Room> findAll() {
    return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
  }

  @Override
  public List<Room> findPublicRooms() {
    return jpaRepository.findByIsPrivateFalse().stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<Room> findByUserId(UUID userId) {
    return jpaRepository.findByUserId(userId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
