package com.chatapp.domain.services;

public class MessageValidator {
  private static final int MAX_MESSAGE_LENGTH = 4000;
  private static final int MIN_MESSAGE_LENGTH = 1;

  public void validate(String content) {
    if (content == null || content.trim().isEmpty()) {
      throw new IllegalArgumentException("Message content cannot be empty");
    }

    if (content.length() > MAX_MESSAGE_LENGTH) {
      throw new IllegalArgumentException(
          "Message content exceeds maximum length of " + MAX_MESSAGE_LENGTH);
    }

    if (content.trim().length() < MIN_MESSAGE_LENGTH) {
      throw new IllegalArgumentException("Message content is too short");
    }
  }
}
