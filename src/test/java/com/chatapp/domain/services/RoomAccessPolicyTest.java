package com.chatapp.domain.services;

import com.chatapp.domain.entities.Room;
import com.chatapp.domain.entities.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RoomAccessPolicyTest {

  private final RoomAccessPolicy policy = new RoomAccessPolicy();

  @Test
  void testCanJoinPublicRoom() {
    UUID userId = UUID.randomUUID();
    User user = User.create("testuser", "test@example.com", "hash");
    user.setId(userId);
    Room room = Room.create("Public Room", "Description", false, UUID.randomUUID());

    assertTrue(policy.canJoinRoom(room, user));
  }

  @Test
  void testCannotJoinPrivateRoomAsNonCreator() {
    UUID userId = UUID.randomUUID();
    User user = User.create("testuser", "test@example.com", "hash");
    user.setId(userId);
    Room room = Room.create("Private Room", "Description", true, UUID.randomUUID());

    assertFalse(policy.canJoinRoom(room, user));
  }

  @Test
  void testCanJoinPrivateRoomAsCreator() {
    UUID userId = UUID.randomUUID();
    User user = User.create("testuser", "test@example.com", "hash");
    user.setId(userId);
    Room room = Room.create("Private Room", "Description", true, userId);

    assertTrue(policy.canJoinRoom(room, user));
  }
}
