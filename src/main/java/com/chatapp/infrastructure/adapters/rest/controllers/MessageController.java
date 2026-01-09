package com.chatapp.infrastructure.adapters.rest.controllers;

import com.chatapp.application.dto.MessageResponse;
import com.chatapp.application.usecases.ListMessagesUseCase;
import com.chatapp.domain.entities.Message;
import com.chatapp.domain.entities.User;
import com.chatapp.domain.ports.UserRepository;
import com.chatapp.infrastructure.security.services.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-auth")
@Tag(name = "Messages", description = "Message history endpoints")
public class MessageController {
  private final ListMessagesUseCase listMessagesUseCase;
  private final UserRepository userRepository;
  private final UserDetailsServiceImpl userDetailsService;

  @GetMapping("/rooms/{roomId}")
  @Operation(summary = "Get message history for a room")
  public ResponseEntity<List<MessageResponse>> getMessages(
      @PathVariable UUID roomId, @RequestParam(required = false) Integer limit, Principal principal) {
    UUID userId = userDetailsService.getUserIdByUsername(principal.getName());
    List<Message> messages = listMessagesUseCase.execute(roomId, userId, limit);

    // Load users for messages
    List<UUID> senderIds =
        messages.stream().map(Message::getSenderId).distinct().collect(Collectors.toList());

    Map<UUID, String> usernames =
        senderIds.stream()
            .collect(
                Collectors.toMap(
                    id -> id,
                    id ->
                        userRepository
                            .findById(id)
                            .map(User::getUsername)
                            .orElse("Unknown")));

    List<MessageResponse> responses =
        messages.stream()
            .map(m -> toResponse(m, usernames.get(m.getSenderId())))
            .collect(Collectors.toList());

    return ResponseEntity.ok(responses);
  }

  private MessageResponse toResponse(Message message, String senderUsername) {
    return MessageResponse.builder()
        .id(message.getId())
        .roomId(message.getRoomId())
        .senderId(message.getSenderId())
        .senderUsername(senderUsername)
        .content(message.getContent())
        .sentAt(message.getSentAt())
        .build();
  }
}
