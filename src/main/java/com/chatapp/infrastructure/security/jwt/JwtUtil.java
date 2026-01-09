package com.chatapp.infrastructure.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  @Value("${app.jwt.secret}")
  private String secret;

  @Value("${app.jwt.expiration-ms}")
  private long expirationMs;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(UUID userId, String username) {
    return Jwts.builder()
        .setSubject(username)
        .claim("userId", userId.toString())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return extractClaims(token).getSubject();
  }

  public UUID extractUserId(String token) {
    String userIdStr = (String) extractClaims(token).get("userId");
    return UUID.fromString(userIdStr);
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractClaims(token).getExpiration().before(new Date());
  }

  private Claims extractClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
