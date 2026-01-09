package com.chatapp.infrastructure.security.services;

import com.chatapp.application.dto.AuthResponse;
import com.chatapp.application.dto.LoginRequest;
import com.chatapp.application.dto.SignupRequest;
import com.chatapp.domain.entities.User;
import com.chatapp.domain.ports.UserRepository;
import com.chatapp.infrastructure.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public AuthResponse signup(SignupRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("Username already exists");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("Email already exists");
    }

    String passwordHash = passwordEncoder.encode(request.getPassword());
    User user = User.create(request.getUsername(), request.getEmail(), passwordHash);
    User saved = userRepository.save(user);

    String token = jwtUtil.generateToken(saved.getId(), saved.getUsername());

    return new AuthResponse(token, saved.getUsername(), saved.getEmail());
  }

  public AuthResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    User user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    String token = jwtUtil.generateToken(user.getId(), user.getUsername());

    return new AuthResponse(token, user.getUsername(), user.getEmail());
  }
}
