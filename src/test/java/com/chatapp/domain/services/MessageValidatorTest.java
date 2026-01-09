package com.chatapp.domain.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageValidatorTest {

  private final MessageValidator validator = new MessageValidator();

  @Test
  void testValidMessage() {
    assertDoesNotThrow(() -> validator.validate("Hello, world!"));
  }

  @Test
  void testNullMessage() {
    assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
  }

  @Test
  void testEmptyMessage() {
    assertThrows(IllegalArgumentException.class, () -> validator.validate(""));
  }

  @Test
  void testTooLongMessage() {
    String longMessage = "a".repeat(4001);
    assertThrows(IllegalArgumentException.class, () -> validator.validate(longMessage));
  }
}
