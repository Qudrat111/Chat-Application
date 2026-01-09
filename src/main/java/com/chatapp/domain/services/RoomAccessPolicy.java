package com.chatapp.domain.services;

import com.chatapp.domain.entities.Room;
import com.chatapp.domain.entities.User;

public class RoomAccessPolicy {
  public boolean canJoinRoom(Room room, User user) {
    if (room == null || user == null) {
      return false;
    }
    // Public rooms can be joined by anyone
    // Private rooms require invitation (simplified: creator can always join)
    return !room.isPrivate() || room.getCreatedBy().equals(user.getId());
  }

  public boolean canSendMessage(Room room, User user, boolean isMember) {
    if (room == null || user == null) {
      return false;
    }
    // Only members can send messages
    return isMember;
  }
}
