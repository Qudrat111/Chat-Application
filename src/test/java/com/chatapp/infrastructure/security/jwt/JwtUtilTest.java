package com.chatapp.infrastructure.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

  private JwtUtil jwtUtil;

  @BeforeEach
  void setUp() {
    jwtUtil = new JwtUtil();
    ReflectionTestUtils.setField(jwtUtil, "secret", "test-secret-key-that-is-long-enough-for-hmac-sha256-algorithm");
    ReflectionTestUtils.setField(jwtUtil, "expirationMs", 3600000L);
  }

  @Test
  void testGenerateAndExtractUsername() {
    UUID userId = UUID.randomUUID();
    String username = "testuser";

    String token = jwtUtil.generateToken(userId, username);
    String extracted = jwtUtil.extractUsername(token);

    assertEquals(username, extracted);
  }

  @Test
  void testExtractUserId() {
    UUID userId = UUID.randomUUID();
    String username = "testuser";

    String token = jwtUtil.generateToken(userId, username);
    UUID extractedId = jwtUtil.extractUserId(token);

    assertEquals(userId, extractedId);
  }

  @Test
  void testValidateToken() {
    UUID userId = UUID.randomUUID();
    String username = "testuser";
    UserDetails userDetails = new User(username, "password", new ArrayList<>());

    String token = jwtUtil.generateToken(userId, username);
    boolean isValid = jwtUtil.validateToken(token, userDetails);

    assertTrue(isValid);
  }
}
