package com.chatapp.application.usecases;

import com.chatapp.application.dto.CreateRoomRequest;
import com.chatapp.domain.entities.Room;
import com.chatapp.domain.entities.User;
import com.chatapp.domain.ports.RoomRepository;
import com.chatapp.domain.ports.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateRoomUseCase {
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;

  @Transactional
  public Room execute(CreateRoomRequest request, UUID userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    Room room =
        Room.create(request.getName(), request.getDescription(), request.isPrivate(), user.getId());

    return roomRepository.save(room);
  }
}
