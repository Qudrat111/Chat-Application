package com.chatapp.infrastructure.adapters.persistence;

import com.chatapp.domain.entities.Membership;
import com.chatapp.domain.ports.MembershipRepository;
import com.chatapp.infrastructure.adapters.persistence.mappers.EntityMapper;
import com.chatapp.infrastructure.adapters.persistence.repositories.JpaMembershipRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MembershipRepositoryAdapter implements MembershipRepository {
  private final JpaMembershipRepository jpaRepository;
  private final EntityMapper mapper;

  @Override
  public Membership save(Membership membership) {
    return mapper.toDomain(jpaRepository.save(mapper.toEntity(membership)));
  }

  @Override
  public Optional<Membership> findByUserIdAndRoomId(UUID userId, UUID roomId) {
    return jpaRepository.findByUserIdAndRoomId(userId, roomId).map(mapper::toDomain);
  }

  @Override
  public List<Membership> findByRoomId(UUID roomId) {
    return jpaRepository.findByRoomId(roomId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<Membership> findByUserId(UUID userId) {
    return jpaRepository.findByUserId(userId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Membership membership) {
    jpaRepository.delete(mapper.toEntity(membership));
  }

  @Override
  public boolean existsByUserIdAndRoomId(UUID userId, UUID roomId) {
    return jpaRepository.existsByUserIdAndRoomId(userId, roomId);
  }
}
