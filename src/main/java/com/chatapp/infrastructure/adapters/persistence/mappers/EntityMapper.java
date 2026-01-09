package com.chatapp.infrastructure.adapters.persistence.mappers;

import com.chatapp.domain.entities.*;
import com.chatapp.infrastructure.adapters.persistence.entities.*;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {
  public UserEntity toEntity(User user) {
    return UserEntity.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .passwordHash(user.getPasswordHash())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }

  public User toDomain(UserEntity entity) {
    return User.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .email(entity.getEmail())
        .passwordHash(entity.getPasswordHash())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  public RoomEntity toEntity(Room room) {
    return RoomEntity.builder()
        .id(room.getId())
        .name(room.getName())
        .description(room.getDescription())
        .isPrivate(room.isPrivate())
        .createdBy(room.getCreatedBy())
        .createdAt(room.getCreatedAt())
        .updatedAt(room.getUpdatedAt())
        .build();
  }

  public Room toDomain(RoomEntity entity) {
    return Room.builder()
        .id(entity.getId())
        .name(entity.getName())
        .description(entity.getDescription())
        .isPrivate(entity.isPrivate())
        .createdBy(entity.getCreatedBy())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  public MembershipEntity toEntity(Membership membership) {
    return MembershipEntity.builder()
        .id(membership.getId())
        .userId(membership.getUserId())
        .roomId(membership.getRoomId())
        .joinedAt(membership.getJoinedAt())
        .build();
  }

  public Membership toDomain(MembershipEntity entity) {
    return Membership.builder()
        .id(entity.getId())
        .userId(entity.getUserId())
        .roomId(entity.getRoomId())
        .joinedAt(entity.getJoinedAt())
        .build();
  }

  public MessageEntity toEntity(Message message) {
    return MessageEntity.builder()
        .id(message.getId())
        .roomId(message.getRoomId())
        .senderId(message.getSenderId())
        .content(message.getContent())
        .sentAt(message.getSentAt())
        .build();
  }

  public Message toDomain(MessageEntity entity) {
    return Message.builder()
        .id(entity.getId())
        .roomId(entity.getRoomId())
        .senderId(entity.getSenderId())
        .content(entity.getContent())
        .sentAt(entity.getSentAt())
        .build();
  }
}
