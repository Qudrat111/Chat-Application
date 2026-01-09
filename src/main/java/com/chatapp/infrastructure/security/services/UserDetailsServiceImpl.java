package com.chatapp.infrastructure.security.services;

import com.chatapp.domain.entities.User;
import com.chatapp.domain.ports.UserRepository;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPasswordHash(), new ArrayList<>());
  }

  public UUID getUserIdByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .map(User::getId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
  }
}
