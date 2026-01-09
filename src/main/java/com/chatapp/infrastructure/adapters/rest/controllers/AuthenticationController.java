package com.chatapp.infrastructure.adapters.rest.controllers;

import com.chatapp.application.dto.AuthResponse;
import com.chatapp.application.dto.LoginRequest;
import com.chatapp.application.dto.SignupRequest;
import com.chatapp.infrastructure.security.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/signup")
  @Operation(summary = "Sign up a new user")
  public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
    return ResponseEntity.ok(authenticationService.signup(request));
  }

  @PostMapping("/login")
  @Operation(summary = "Log in a user")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authenticationService.login(request));
  }
}
