package com.chatapp.infrastructure.adapters.rest.controllers;

import com.chatapp.application.dto.CreateRoomRequest;
import com.chatapp.application.dto.RoomResponse;
import com.chatapp.application.usecases.CreateRoomUseCase;
import com.chatapp.application.usecases.JoinRoomUseCase;
import com.chatapp.application.usecases.LeaveRoomUseCase;
import com.chatapp.domain.entities.Room;
import com.chatapp.domain.ports.RoomRepository;
import com.chatapp.infrastructure.security.services.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-auth")
@Tag(name = "Rooms", description = "Room management endpoints")
public class RoomController {
  private final CreateRoomUseCase createRoomUseCase;
  private final JoinRoomUseCase joinRoomUseCase;
  private final LeaveRoomUseCase leaveRoomUseCase;
  private final RoomRepository roomRepository;
  private final UserDetailsServiceImpl userDetailsService;

  @PostMapping
  @Operation(summary = "Create a new room")
  public ResponseEntity<RoomResponse> createRoom(
      @Valid @RequestBody CreateRoomRequest request, Principal principal) {
    UUID userId = userDetailsService.getUserIdByUsername(principal.getName());
    Room room = createRoomUseCase.execute(request, userId);
    return ResponseEntity.ok(toResponse(room));
  }

  @GetMapping
  @Operation(summary = "Get all public rooms")
  public ResponseEntity<List<RoomResponse>> getPublicRooms() {
    List<RoomResponse> rooms =
        roomRepository.findPublicRooms().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    return ResponseEntity.ok(rooms);
  }

  @GetMapping("/my-rooms")
  @Operation(summary = "Get user's rooms")
  public ResponseEntity<List<RoomResponse>> getMyRooms(Principal principal) {
    UUID userId = userDetailsService.getUserIdByUsername(principal.getName());
    List<RoomResponse> rooms =
        roomRepository.findByUserId(userId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    return ResponseEntity.ok(rooms);
  }

  @PostMapping("/{roomId}/join")
  @Operation(summary = "Join a room")
  public ResponseEntity<Void> joinRoom(@PathVariable UUID roomId, Principal principal) {
    UUID userId = userDetailsService.getUserIdByUsername(principal.getName());
    joinRoomUseCase.execute(roomId, userId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{roomId}/leave")
  @Operation(summary = "Leave a room")
  public ResponseEntity<Void> leaveRoom(@PathVariable UUID roomId, Principal principal) {
    UUID userId = userDetailsService.getUserIdByUsername(principal.getName());
    leaveRoomUseCase.execute(roomId, userId);
    return ResponseEntity.ok().build();
  }

  private RoomResponse toResponse(Room room) {
    return RoomResponse.builder()
        .id(room.getId())
        .name(room.getName())
        .description(room.getDescription())
        .isPrivate(room.isPrivate())
        .createdBy(room.getCreatedBy())
        .createdAt(room.getCreatedAt())
        .build();
  }
}
