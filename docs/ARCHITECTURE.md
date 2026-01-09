# Architecture Diagrams

This document contains PlantUML text diagrams showing the architecture and key flows of the Chat Application.

## System Architecture

```plantuml
@startuml Chat Application Architecture
!define RECTANGLE class

skinparam rectangle {
    BackgroundColor<<domain>> LightBlue
    BackgroundColor<<application>> LightGreen
    BackgroundColor<<infrastructure>> LightYellow
    BackgroundColor<<external>> LightGray
}

package "Presentation Layer" {
    rectangle "REST Controllers" as REST
    rectangle "WebSocket Controllers" as WS
}

package "Application Layer" <<application>> {
    rectangle "Use Cases" as UC {
        rectangle "CreateRoom" as UC1
        rectangle "JoinRoom" as UC2
        rectangle "SendMessage" as UC3
        rectangle "ListMessages" as UC4
        rectangle "PublishTyping" as UC5
    }
}

package "Domain Layer" <<domain>> {
    rectangle "Entities" as ENT {
        rectangle "User"
        rectangle "Room"
        rectangle "Message"
        rectangle "Membership"
        rectangle "TypingIndicator"
    }
    rectangle "Domain Services" as DS {
        rectangle "MessageValidator"
        rectangle "RoomAccessPolicy"
    }
    rectangle "Ports" as PORTS {
        interface "UserRepository"
        interface "RoomRepository"
        interface "MessageRepository"
        interface "MessagingService"
    }
}

package "Infrastructure Layer" <<infrastructure>> {
    rectangle "JPA Repositories" as JPA
    rectangle "Redis Messaging" as REDIS
    rectangle "Security (JWT)" as SEC
}

package "External Systems" <<external>> {
    database "PostgreSQL" as PG
    database "Redis" as RD
}

REST --> UC
WS --> UC
UC --> DS
UC --> ENT
UC --> PORTS
PORTS <|.. JPA
PORTS <|.. REDIS
JPA --> PG
REDIS --> RD
REST --> SEC
WS --> SEC

@enduml
```

## Authentication Sequence

```plantuml
@startuml Authentication Flow
actor User
participant "REST Controller" as REST
participant "AuthenticationService" as AUTH
participant "UserRepository" as REPO
participant "JwtUtil" as JWT
participant "PasswordEncoder" as PWD
database "PostgreSQL" as DB

== Signup Flow ==
User -> REST: POST /api/auth/signup\n(username, email, password)
REST -> AUTH: signup(request)
AUTH -> REPO: existsByUsername(username)
REPO -> DB: SELECT * FROM users WHERE username=?
DB --> REPO: exists=false
REPO --> AUTH: false
AUTH -> PWD: encode(password)
PWD --> AUTH: passwordHash
AUTH -> REPO: save(user)
REPO -> DB: INSERT INTO users
DB --> REPO: saved user
REPO --> AUTH: user
AUTH -> JWT: generateToken(userId, username)
JWT --> AUTH: token
AUTH --> REST: AuthResponse(token, username, email)
REST --> User: 200 OK + JWT token

== Login Flow ==
User -> REST: POST /api/auth/login\n(username, password)
REST -> AUTH: login(request)
AUTH -> AUTH: authenticate(username, password)
AUTH -> REPO: findByUsername(username)
REPO -> DB: SELECT * FROM users WHERE username=?
DB --> REPO: user
REPO --> AUTH: user
AUTH -> PWD: matches(password, passwordHash)
PWD --> AUTH: true
AUTH -> JWT: generateToken(userId, username)
JWT --> AUTH: token
AUTH --> REST: AuthResponse(token)
REST --> User: 200 OK + JWT token

@enduml
```

## Send Message Sequence

```plantuml
@startuml Send Message Flow
actor User
participant "WebSocket Client" as WS
participant "ChatWebSocketController" as CTRL
participant "SendMessageUseCase" as UC
participant "MessageValidator" as VAL
participant "MessageRepository" as REPO
participant "MessagingService" as MSG
participant "STOMP Broker" as BROKER
database "PostgreSQL" as DB
database "Redis" as REDIS

User -> WS: Type message
WS -> CTRL: SEND /app/rooms/{roomId}/messages\n+ JWT Token
CTRL -> UC: execute(roomId, userId, content)
UC -> VAL: validate(content)
VAL --> UC: OK
UC -> REPO: save(message)
REPO -> DB: INSERT INTO messages
DB --> REPO: saved message
REPO --> UC: message
UC -> MSG: publishMessage(message)
MSG -> REDIS: PUBLISH chat.room.{roomId}
REDIS --> MSG: OK
MSG --> UC: OK
UC --> CTRL: message
CTRL -> BROKER: convertAndSend(/topic/rooms/{roomId}, message)
BROKER --> WS: Message broadcasted
WS --> User: Display message

@enduml
```

## Create and Join Room Sequence

```plantuml
@startuml Room Management Flow
actor User1
actor User2
participant "REST API" as REST
participant "CreateRoomUseCase" as CREATE
participant "JoinRoomUseCase" as JOIN
participant "RoomAccessPolicy" as POLICY
participant "Repositories" as REPO
database "PostgreSQL" as DB

== Create Room ==
User1 -> REST: POST /api/rooms\n{name, description, isPrivate}
REST -> CREATE: execute(request, userId)
CREATE -> REPO: save(room)
REPO -> DB: INSERT INTO rooms
DB --> REPO: room
REPO --> CREATE: room
CREATE --> REST: room
REST --> User1: 201 Created + room details

== Join Room ==
User2 -> REST: POST /api/rooms/{roomId}/join
REST -> JOIN: execute(roomId, userId)
JOIN -> REPO: findRoomById(roomId)
REPO -> DB: SELECT * FROM rooms
DB --> REPO: room
REPO --> JOIN: room
JOIN -> REPO: findUserById(userId)
REPO -> DB: SELECT * FROM users
DB --> REPO: user
REPO --> JOIN: user
JOIN -> POLICY: canJoinRoom(room, user)
POLICY --> JOIN: true
JOIN -> REPO: existsMembership(userId, roomId)
REPO -> DB: SELECT * FROM memberships
DB --> REPO: false
REPO --> JOIN: false
JOIN -> REPO: save(membership)
REPO -> DB: INSERT INTO memberships
DB --> REPO: membership
REPO --> JOIN: membership
JOIN --> REST: membership
REST --> User2: 200 OK

@enduml
```

## WebSocket Connection Flow

```plantuml
@startuml WebSocket Connection
actor User
participant "WebSocket Client" as CLIENT
participant "WebSocket Server\n(/ws)" as WS
participant "JwtAuthenticationFilter" as JWT
participant "STOMP Broker" as BROKER
participant "ChatWebSocketController" as CTRL

User -> CLIENT: Connect to chat
CLIENT -> WS: WebSocket Handshake\n+ JWT in headers
WS -> JWT: validateToken(token)
JWT --> WS: authenticated
WS --> CLIENT: Connection established

CLIENT -> BROKER: SUBSCRIBE /topic/rooms/{roomId}
BROKER --> CLIENT: Subscription confirmed

CLIENT -> BROKER: SUBSCRIBE /topic/rooms/{roomId}/typing
BROKER --> CLIENT: Subscription confirmed

== Send Message ==
User -> CLIENT: Type and send message
CLIENT -> CTRL: SEND /app/rooms/{roomId}/messages
CTRL -> BROKER: Broadcast to /topic/rooms/{roomId}
BROKER --> CLIENT: Message received
CLIENT --> User: Display message

== Typing Indicator ==
User -> CLIENT: Start typing
CLIENT -> CTRL: SEND /app/rooms/{roomId}/typing\n{typing: true}
CTRL -> BROKER: Broadcast to /topic/rooms/{roomId}/typing
BROKER --> CLIENT: Typing indicator
CLIENT --> User: Show "User is typing..."

User -> CLIENT: Stop typing
CLIENT -> CTRL: SEND /app/rooms/{roomId}/typing\n{typing: false}
CTRL -> BROKER: Broadcast to /topic/rooms/{roomId}/typing
BROKER --> CLIENT: Typing indicator
CLIENT --> User: Hide "User is typing..."

@enduml
```

## Component Diagram

```plantuml
@startuml Component Diagram
package "Chat Application" {
    [REST API] as REST
    [WebSocket API] as WS
    [Security Layer] as SEC
    [Use Cases] as UC
    [Domain Model] as DOM
    [JPA Adapters] as JPA
    [Redis Adapter] as REDIS
}

database "PostgreSQL" as PG
database "Redis" as RD

[Web Client] --> REST : HTTP/HTTPS
[Web Client] --> WS : WebSocket
REST --> SEC : JWT Validation
WS --> SEC : JWT Validation
REST --> UC : Execute
WS --> UC : Execute
UC --> DOM : Use
UC --> JPA : Persist
UC --> REDIS : Publish
JPA --> PG : JDBC
REDIS --> RD : Redis Protocol

note right of SEC
  - JWT Generation
  - Token Validation
  - BCrypt Hashing
end note

note right of UC
  - CreateRoom
  - JoinRoom
  - SendMessage
  - ListMessages
  - PublishTyping
end note

note right of DOM
  - User
  - Room
  - Message
  - Membership
  - Validators
  - Policies
end note

@enduml
```

## Database Schema

```plantuml
@startuml Database Schema
entity "users" as users {
  * id : UUID <<PK>>
  --
  * username : VARCHAR(50) <<UNIQUE>>
  * email : VARCHAR(255) <<UNIQUE>>
  * password_hash : VARCHAR(255)
  * created_at : TIMESTAMP
  * updated_at : TIMESTAMP
}

entity "rooms" as rooms {
  * id : UUID <<PK>>
  --
  * name : VARCHAR(100)
  description : VARCHAR(500)
  * is_private : BOOLEAN
  * created_by : UUID <<FK>>
  * created_at : TIMESTAMP
  * updated_at : TIMESTAMP
}

entity "memberships" as memberships {
  * id : UUID <<PK>>
  --
  * user_id : UUID <<FK>>
  * room_id : UUID <<FK>>
  * joined_at : TIMESTAMP
}

entity "messages" as messages {
  * id : UUID <<PK>>
  --
  * room_id : UUID <<FK>>
  * sender_id : UUID <<FK>>
  * content : VARCHAR(4000)
  * sent_at : TIMESTAMP
}

users ||--o{ rooms : creates
users ||--o{ memberships : joins
rooms ||--o{ memberships : has
users ||--o{ messages : sends
rooms ||--o{ messages : contains

@enduml
```

## Deployment Diagram

```plantuml
@startuml Deployment Architecture
node "Docker Host" {
    node "chat-app Container" {
        component "Spring Boot App" as APP
        component "Embedded Tomcat" as TOMCAT
    }
    
    node "postgres Container" {
        database "PostgreSQL" as PG
    }
    
    node "redis Container" {
        database "Redis" as REDIS
    }
}

cloud "Internet" {
    actor "Web Browser" as BROWSER
    actor "Mobile App" as MOBILE
}

BROWSER --> APP : HTTP/HTTPS
BROWSER --> APP : WebSocket
MOBILE --> APP : HTTP/HTTPS
MOBILE --> APP : WebSocket

APP --> PG : JDBC Connection
APP --> REDIS : Redis Protocol

note right of APP
  Port: 8080
  Environment: dev/prod
  JVM: Java 17
end note

note right of PG
  Port: 5432
  Database: chatdb
  Version: 16-alpine
end note

note right of REDIS
  Port: 6379
  Version: 7-alpine
end note

@enduml
```

## Class Diagram - Domain Layer

```plantuml
@startuml Domain Layer Classes
class User {
  - id: UUID
  - username: String
  - email: String
  - passwordHash: String
  - createdAt: Instant
  - updatedAt: Instant
  + create(username, email, passwordHash): User
}

class Room {
  - id: UUID
  - name: String
  - description: String
  - isPrivate: boolean
  - createdBy: UUID
  - createdAt: Instant
  - updatedAt: Instant
  + create(name, description, isPrivate, createdBy): Room
}

class Membership {
  - id: UUID
  - userId: UUID
  - roomId: UUID
  - joinedAt: Instant
  + create(userId, roomId): Membership
}

class Message {
  - id: UUID
  - roomId: UUID
  - senderId: UUID
  - content: String
  - sentAt: Instant
  + create(roomId, senderId, content): Message
}

class TypingIndicator {
  - roomId: UUID
  - userId: UUID
  - username: String
  - typing: boolean
  - timestamp: Instant
  + create(roomId, userId, username, typing): TypingIndicator
}

class MessageValidator {
  + validate(content: String): void
}

class RoomAccessPolicy {
  + canJoinRoom(room: Room, user: User): boolean
  + canSendMessage(room: Room, user: User, isMember: boolean): boolean
}

interface UserRepository {
  + save(user: User): User
  + findById(id: UUID): Optional<User>
  + findByUsername(username: String): Optional<User>
}

interface RoomRepository {
  + save(room: Room): Room
  + findById(id: UUID): Optional<Room>
  + findAll(): List<Room>
}

interface MessageRepository {
  + save(message: Message): Message
  + findByRoomId(roomId: UUID): List<Message>
}

interface MessagingService {
  + publishMessage(message: Message): void
  + publishTypingIndicator(indicator: TypingIndicator): void
}

User "1" -- "0..*" Membership : joins rooms
Room "1" -- "0..*" Membership : has members
User "1" -- "0..*" Message : sends
Room "1" -- "0..*" Message : contains

@enduml
```

## Notes on Diagrams

To render these PlantUML diagrams:

1. Use an online PlantUML editor: http://www.plantuml.com/plantuml
2. Install PlantUML locally with Graphviz
3. Use IDE plugins (IntelliJ IDEA, VS Code)
4. Use command line: `plantuml diagram.puml`

The diagrams illustrate:

- **System Architecture**: Overall layered architecture
- **Authentication Sequence**: Signup and login flows
- **Send Message Sequence**: Real-time message delivery
- **Room Management**: Creating and joining rooms
- **WebSocket Connection**: STOMP protocol flow
- **Component Diagram**: High-level components
- **Database Schema**: Entity relationships
- **Deployment**: Docker container architecture
- **Domain Classes**: Core business entities and interfaces
