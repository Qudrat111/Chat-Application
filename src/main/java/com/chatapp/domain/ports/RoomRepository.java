package com.chatapp.domain.ports;

import com.chatapp.domain.entities.Room;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {
  Room save(Room room);

  Optional<Room> findById(UUID id);

  List<Room> findAll();

  List<Room> findPublicRooms();

  List<Room> findByUserId(UUID userId);

  void deleteById(UUID id);
}
