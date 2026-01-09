package com.chatapp.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Chat Application API",
            version = "1.0",
            description = "Production-ready Spring Boot 3 WebSocket/STOMP chat application",
            contact = @Contact(name = "Chat App Team")),
    servers = {@Server(url = "http://localhost:8080", description = "Local server")})
@SecurityScheme(
    name = "bearer-auth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT")
public class OpenApiConfig {}
