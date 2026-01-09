package com.chatapp.infrastructure.adapters.persistence;

import com.chatapp.domain.entities.User;
import com.chatapp.domain.ports.UserRepository;
import com.chatapp.infrastructure.adapters.persistence.mappers.EntityMapper;
import com.chatapp.infrastructure.adapters.persistence.repositories.JpaUserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
  private final JpaUserRepository jpaRepository;
  private final EntityMapper mapper;

  @Override
  public User save(User user) {
    return mapper.toDomain(jpaRepository.save(mapper.toEntity(user)));
  }

  @Override
  public Optional<User> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return jpaRepository.findByUsername(username).map(mapper::toDomain);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return jpaRepository.findByEmail(email).map(mapper::toDomain);
  }

  @Override
  public boolean existsByUsername(String username) {
    return jpaRepository.existsByUsername(username);
  }

  @Override
  public boolean existsByEmail(String email) {
    return jpaRepository.existsByEmail(email);
  }
}
