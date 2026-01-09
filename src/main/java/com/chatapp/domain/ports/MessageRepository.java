package com.chatapp.domain.ports;

import com.chatapp.domain.entities.Message;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
  Message save(Message message);

  List<Message> findByRoomId(UUID roomId);

  List<Message> findByRoomIdWithLimit(UUID roomId, int limit);
}
