# Production-Ready Spring Boot 3 WebSocket/STOMP Chat Application

A comprehensive, production-ready real-time chat application built with Spring Boot 3, WebSocket/STOMP, Redis, and PostgreSQL following clean/hexagonal architecture principles.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [WebSocket/STOMP Protocol](#websocketstomp-protocol)
- [Security](#security)
- [Testing](#testing)
- [Deployment](#deployment)
- [Design Decisions](#design-decisions)
- [Contributing](#contributing)

## Features

- **Real-time Messaging**: WebSocket/STOMP-based real-time communication
- **Authentication & Authorization**: JWT-based secure authentication
- **Room Management**: Create and manage public/private chat rooms
- **Typing Indicators**: Real-time typing status notifications
- **Message History**: Persistent message storage with PostgreSQL
- **Scalability**: Redis pub/sub for horizontal scaling
- **Clean Architecture**: Domain-driven design with hexagonal architecture
- **Production Ready**: Observability, monitoring, code quality tools
- **Comprehensive Testing**: Unit, integration, and contract tests

## Architecture

This application follows Clean/Hexagonal Architecture principles:

```
┌─────────────────────────────────────────────────────────────┐
│                        Presentation Layer                    │
│   ┌─────────────────┐           ┌──────────────────┐        │
│   │  REST Controllers│           │ WebSocket/STOMP  │        │
│   │  (Auth, Rooms,  │           │   Controllers    │        │
│   │   Messages)     │           │ (Chat, Typing)   │        │
│   └────────┬────────┘           └─────────┬────────┘        │
└────────────┼──────────────────────────────┼─────────────────┘
             │                              │
             v                              v
┌─────────────────────────────────────────────────────────────┐
│                      Application Layer                       │
│   ┌──────────────────────────────────────────────────┐      │
│   │  Use Cases (Business Logic)                      │      │
│   │  - CreateRoom, JoinRoom, LeaveRoom              │      │
│   │  - SendMessage, ListMessages, PublishTyping     │      │
│   └──────────────────┬───────────────────────────────┘      │
└────────────────────┼─┼──────────────────────────────────────┘
                     │ │
                     v v
┌─────────────────────────────────────────────────────────────┐
│                        Domain Layer                          │
│   ┌─────────────┐  ┌──────────────┐  ┌──────────────┐      │
│   │  Entities   │  │   Services   │  │    Events    │      │
│   │  User, Room │  │  Validators  │  │  MessageSent │      │
│   │  Message,   │  │   Policies   │  │  UserJoined  │      │
│   │  Membership │  │              │  │              │      │
│   └─────────────┘  └──────────────┘  └──────────────┘      │
│   ┌─────────────────────────────────────────────────┐      │
│   │  Ports (Interfaces)                             │      │
│   │  - UserRepository, RoomRepository               │      │
│   │  - MessageRepository, MessagingService          │      │
│   └─────────────────────────────────────────────────┘      │
└────────────────────────────┬────────────────────────────────┘
                             │
                             v
┌─────────────────────────────────────────────────────────────┐
│                    Infrastructure Layer                      │
│   ┌──────────────┐  ┌──────────────┐  ┌─────────────────┐  │
│   │ Persistence  │  │   Security   │  │    Messaging    │  │
│   │  (JPA/PG)    │  │  (JWT/BCrypt)│  │  (Redis Pub/Sub)│  │
│   └──────────────┘  └──────────────┘  └─────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Architecture Layers

1. **Domain Layer**: Core business logic, entities, and rules
2. **Application Layer**: Use cases orchestrating domain logic
3. **Infrastructure Layer**: External dependencies (DB, messaging, security)
4. **Presentation Layer**: REST and WebSocket endpoints

## Tech Stack

- **Backend Framework**: Spring Boot 3.2.1
- **Language**: Java 17
- **Real-time Communication**: WebSocket + STOMP
- **Security**: Spring Security + JWT
- **Database**: PostgreSQL
- **Caching & Messaging**: Redis
- **Database Migration**: Flyway
- **Build Tool**: Maven
- **Code Quality**: Spotless, Checkstyle
- **Testing**: JUnit 5, Mockito, Testcontainers
- **API Documentation**: OpenAPI/Swagger
- **Observability**: Micrometer, Prometheus
- **Containerization**: Docker, Docker Compose

## Prerequisites

- **Java 17** or higher
- **Maven 3.8+** (or use included Maven wrapper)
- **Docker** and **Docker Compose** (for database and Redis)
- **Git**

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/Qudrat111/Chat-Application.git
cd Chat-Application
```

### 2. Start Dependencies (PostgreSQL + Redis)

```bash
docker-compose up -d db redis
```

### 3. Run Tests

```bash
./mvnw test
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### 5. Access API Documentation

Visit `http://localhost:8080/swagger-ui.html` for interactive API documentation

### Alternative: Run Everything with Docker

```bash
# Build and start all services
make docker-up

# Or manually:
./mvnw clean package -DskipTests
docker-compose up
```

## Project Structure

```
Chat-Application/
├── src/
│   ├── main/
│   │   ├── java/com/chatapp/
│   │   │   ├── domain/
│   │   │   │   ├── entities/        # Domain entities
│   │   │   │   ├── services/        # Domain services
│   │   │   │   ├── events/          # Domain events
│   │   │   │   └── ports/           # Repository interfaces
│   │   │   ├── application/
│   │   │   │   ├── usecases/        # Business use cases
│   │   │   │   └── dto/             # Data transfer objects
│   │   │   └── infrastructure/
│   │   │       ├── adapters/
│   │   │       │   ├── rest/        # REST controllers
│   │   │       │   ├── websocket/   # WebSocket controllers
│   │   │       │   ├── persistence/ # JPA repositories
│   │   │       │   └── messaging/   # Redis messaging
│   │   │       ├── config/          # Configuration classes
│   │   │       └── security/        # Security & JWT
│   │   └── resources/
│   │       ├── application.yml      # Application configuration
│   │       └── db/migration/        # Flyway migrations
│   └── test/                        # Test files
├── docs/                            # Additional documentation
├── docker-compose.yml               # Docker Compose configuration
├── Dockerfile                       # Application Docker image
├── Makefile                         # Convenient commands
├── .env                             # Environment variables
├── checkstyle.xml                   # Code style rules
└── pom.xml                          # Maven configuration
```

## API Documentation

### REST Endpoints

#### Authentication

**POST /api/auth/signup**
```json
Request:
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securepass123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "john_doe",
  "email": "john@example.com"
}
```

**POST /api/auth/login**
```json
Request:
{
  "username": "john_doe",
  "password": "securepass123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "john_doe",
  "email": "john@example.com"
}
```

#### Room Management

**POST /api/rooms** (Authenticated)
```json
Request:
{
  "name": "General Discussion",
  "description": "A room for general topics",
  "isPrivate": false
}

Response:
{
  "id": "uuid-here",
  "name": "General Discussion",
  "description": "A room for general topics",
  "isPrivate": false,
  "createdBy": "user-uuid",
  "createdAt": "2026-01-09T10:00:00Z"
}
```

**GET /api/rooms** (Authenticated)
- Returns list of all public rooms

**GET /api/rooms/my-rooms** (Authenticated)
- Returns rooms the user is a member of

**POST /api/rooms/{roomId}/join** (Authenticated)
- Join a specific room

**POST /api/rooms/{roomId}/leave** (Authenticated)
- Leave a specific room

#### Message History

**GET /api/messages/rooms/{roomId}?limit=50** (Authenticated)
```json
Response:
[
  {
    "id": "message-uuid",
    "roomId": "room-uuid",
    "senderId": "user-uuid",
    "senderUsername": "john_doe",
    "content": "Hello, everyone!",
    "sentAt": "2026-01-09T10:05:00Z"
  }
]
```

## WebSocket/STOMP Protocol

### Connection

1. **Connect to WebSocket endpoint**: `ws://localhost:8080/ws`
2. **Use STOMP client** (e.g., @stomp/stompjs for JavaScript)

### Subscribe to Topics

**Subscribe to room messages**:
```
SUBSCRIBE /topic/rooms/{roomId}
```

**Subscribe to typing indicators**:
```
SUBSCRIBE /topic/rooms/{roomId}/typing
```

### Send Messages

**Send a chat message**:
```
SEND /app/rooms/{roomId}/messages
Content-Type: application/json
Authorization: Bearer {JWT_TOKEN}

{"content": "Hello, world!"}
```

**Send typing indicator**:
```
SEND /app/rooms/{roomId}/typing
Content-Type: application/json
Authorization: Bearer {JWT_TOKEN}

{"username": "john_doe", "typing": true}
```

### Example: JavaScript Client

```javascript
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const socket = new SockJS('http://localhost:8080/ws');
const stompClient = new Client({
  webSocketFactory: () => socket,
  connectHeaders: {
    Authorization: `Bearer ${jwtToken}`
  },
  onConnect: () => {
    // Subscribe to room messages
    stompClient.subscribe('/topic/rooms/' + roomId, (message) => {
      const msg = JSON.parse(message.body);
      console.log('Received:', msg);
    });

    // Subscribe to typing indicators
    stompClient.subscribe('/topic/rooms/' + roomId + '/typing', (message) => {
      const indicator = JSON.parse(message.body);
      console.log('Typing:', indicator);
    });
  }
});

stompClient.activate();

// Send a message
const sendMessage = (content) => {
  stompClient.publish({
    destination: '/app/rooms/' + roomId + '/messages',
    body: JSON.stringify({ content }),
    headers: { Authorization: `Bearer ${jwtToken}` }
  });
};

// Send typing indicator
const sendTyping = (isTyping) => {
  stompClient.publish({
    destination: '/app/rooms/' + roomId + '/typing',
    body: JSON.stringify({ username: 'john_doe', typing: isTyping }),
    headers: { Authorization: `Bearer ${jwtToken}` }
  });
};
```

## Security

### Authentication Flow

1. User signs up or logs in via REST API
2. Server validates credentials and returns JWT token
3. Client includes JWT token in all subsequent requests:
   - REST: `Authorization: Bearer {token}` header
   - WebSocket: Token in connection headers

### Password Security

- Passwords are hashed using BCrypt (strength 10)
- Never stored in plain text
- Validated on login using secure comparison

### JWT Token

- Signed with HS256 algorithm
- Contains user ID and username
- Default expiration: 24 hours (configurable)
- Secret key stored in environment variables

### CORS Configuration

- Configurable allowed origins (default: localhost:3000, localhost:4200)
- Supports credentials
- All HTTP methods allowed for development

## Testing

### Run All Tests

```bash
./mvnw test
```

### Test Categories

1. **Unit Tests**: Domain services, validators, policies
2. **Integration Tests**: Application context loading
3. **Security Tests**: JWT generation and validation

### Test Coverage

- Domain layer: MessageValidator, RoomAccessPolicy
- Infrastructure: JwtUtil
- Application: Context loading

## Deployment

### Local Development

```bash
# Start databases
docker-compose up -d db redis

# Run application
./mvnw spring-boot:run
```

### Production Docker Deployment

```bash
# Build application
./mvnw clean package -DskipTests

# Build and run all services
docker-compose up -d

# Check logs
docker-compose logs -f app
```

### Environment Variables

Configure these in `.env` or as environment variables:

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=chatdb
DB_USERNAME=chatuser
DB_PASSWORD=chatpass
REDIS_HOST=localhost
REDIS_PORT=6379
JWT_SECRET=your-secure-secret-key-here
JWT_EXPIRATION_MS=86400000
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

## Design Decisions

### Why Clean/Hexagonal Architecture?

- **Testability**: Business logic isolated from frameworks
- **Maintainability**: Clear separation of concerns
- **Flexibility**: Easy to swap infrastructure components
- **Domain Focus**: Core business rules independent of technical details

### Why WebSocket/STOMP over Server-Sent Events?

- **Bi-directional**: Full-duplex communication
- **Protocol Support**: STOMP provides messaging semantics
- **Scalability**: Easy to integrate with message brokers

### Why Redis for Messaging?

- **Horizontal Scaling**: Pub/sub enables multi-instance deployment
- **Performance**: In-memory operations for real-time needs
- **Presence Management**: Track online users across instances

### Why PostgreSQL over NoSQL?

- **ACID Compliance**: Ensures data consistency
- **Relational Model**: Natural fit for user-room-message relationships
- **Rich Queries**: Complex queries for message history

### Why JWT over Session-based Auth?

- **Stateless**: No server-side session storage needed
- **Scalable**: Works across multiple application instances
- **Mobile-Friendly**: Easy to use in mobile apps

## Common Tasks

```bash
# Format code
make format
# or
./mvnw spotless:apply

# Build without tests
./mvnw clean package -DskipTests

# Run specific test
./mvnw test -Dtest=JwtUtilTest

# Clean everything
make clean
docker-compose down -v
```

## Monitoring and Observability

- **Actuator Endpoints**: `/actuator/health`, `/actuator/metrics`, `/actuator/prometheus`
- **Structured Logging**: JSON format for production
- **Metrics**: Micrometer integration with Prometheus

Access metrics at: `http://localhost:8080/actuator/prometheus`

## Troubleshooting

### Application won't start

1. Check if PostgreSQL and Redis are running:
   ```bash
   docker-compose ps
   ```

2. Verify database connection:
   ```bash
   docker-compose logs db
   ```

3. Check application logs for errors

### WebSocket connection fails

1. Verify JWT token is valid
2. Check CORS configuration in `application.yml`
3. Ensure WebSocket endpoint is `/ws`

### Tests failing

1. Ensure H2 database dependency is in pom.xml
2. Check test profile configuration in `src/test/resources/application-test.yml`

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is created for educational and demonstration purposes.

## Contact

For questions or support, please open an issue on GitHub.