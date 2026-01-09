# API Usage Examples

This document provides comprehensive examples of using the Chat Application API.

## Table of Contents

- [Authentication](#authentication)
- [Room Management](#room-management)
- [Messaging](#messaging)
- [WebSocket/STOMP](#websocketstomp)
- [Error Handling](#error-handling)

## Authentication

### Signup

Create a new user account.

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "SecurePass123!"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImlhdCI6MTcwNDc5MjAwMCwiZXhwIjoxNzA0ODc4NDAwfQ.example",
  "type": "Bearer",
  "username": "john_doe",
  "email": "john@example.com"
}
```

### Login

Authenticate an existing user.

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePass123!"
  }'
```

**JavaScript Fetch Example:**
```javascript
const login = async (username, password) => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ username, password })
  });
  
  if (!response.ok) {
    throw new Error('Login failed');
  }
  
  const data = await response.json();
  localStorage.setItem('jwt-token', data.token);
  return data;
};

// Usage
login('john_doe', 'SecurePass123!')
  .then(data => console.log('Logged in:', data))
  .catch(error => console.error('Error:', error));
```

## Room Management

### Create a Room

**cURL Example:**
```bash
TOKEN="your-jwt-token-here"

curl -X POST http://localhost:8080/api/rooms \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "General Discussion",
    "description": "A place for general chat",
    "isPrivate": false
  }'
```

**JavaScript Fetch Example:**
```javascript
const createRoom = async (name, description, isPrivate) => {
  const token = localStorage.getItem('jwt-token');
  
  const response = await fetch('http://localhost:8080/api/rooms', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({ name, description, isPrivate })
  });
  
  if (!response.ok) {
    throw new Error('Failed to create room');
  }
  
  return await response.json();
};

// Usage
createRoom('General Discussion', 'A place for general chat', false)
  .then(room => console.log('Room created:', room))
  .catch(error => console.error('Error:', error));
```

**Response:**
```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "name": "General Discussion",
  "description": "A place for general chat",
  "isPrivate": false,
  "createdBy": "123e4567-e89b-12d3-a456-426614174000",
  "createdAt": "2026-01-09T10:00:00Z"
}
```

### List Public Rooms

**cURL Example:**
```bash
TOKEN="your-jwt-token-here"

curl -X GET http://localhost:8080/api/rooms \
  -H "Authorization: Bearer $TOKEN"
```

**JavaScript Fetch Example:**
```javascript
const getPublicRooms = async () => {
  const token = localStorage.getItem('jwt-token');
  
  const response = await fetch('http://localhost:8080/api/rooms', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  return await response.json();
};

// Usage
getPublicRooms()
  .then(rooms => console.log('Public rooms:', rooms))
  .catch(error => console.error('Error:', error));
```

### List My Rooms

**cURL Example:**
```bash
TOKEN="your-jwt-token-here"

curl -X GET http://localhost:8080/api/rooms/my-rooms \
  -H "Authorization: Bearer $TOKEN"
```

### Join a Room

**cURL Example:**
```bash
TOKEN="your-jwt-token-here"
ROOM_ID="a1b2c3d4-e5f6-7890-abcd-ef1234567890"

curl -X POST "http://localhost:8080/api/rooms/$ROOM_ID/join" \
  -H "Authorization: Bearer $TOKEN"
```

**JavaScript Example:**
```javascript
const joinRoom = async (roomId) => {
  const token = localStorage.getItem('jwt-token');
  
  const response = await fetch(`http://localhost:8080/api/rooms/${roomId}/join`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  if (!response.ok) {
    throw new Error('Failed to join room');
  }
};

// Usage
joinRoom('a1b2c3d4-e5f6-7890-abcd-ef1234567890')
  .then(() => console.log('Joined room successfully'))
  .catch(error => console.error('Error:', error));
```

### Leave a Room

**cURL Example:**
```bash
TOKEN="your-jwt-token-here"
ROOM_ID="a1b2c3d4-e5f6-7890-abcd-ef1234567890"

curl -X POST "http://localhost:8080/api/rooms/$ROOM_ID/leave" \
  -H "Authorization: Bearer $TOKEN"
```

## Messaging

### Get Message History

**cURL Example:**
```bash
TOKEN="your-jwt-token-here"
ROOM_ID="a1b2c3d4-e5f6-7890-abcd-ef1234567890"

# Get all messages
curl -X GET "http://localhost:8080/api/messages/rooms/$ROOM_ID" \
  -H "Authorization: Bearer $TOKEN"

# Get last 50 messages
curl -X GET "http://localhost:8080/api/messages/rooms/$ROOM_ID?limit=50" \
  -H "Authorization: Bearer $TOKEN"
```

**JavaScript Example:**
```javascript
const getMessages = async (roomId, limit = null) => {
  const token = localStorage.getItem('jwt-token');
  const url = limit 
    ? `http://localhost:8080/api/messages/rooms/${roomId}?limit=${limit}`
    : `http://localhost:8080/api/messages/rooms/${roomId}`;
  
  const response = await fetch(url, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  return await response.json();
};

// Usage
getMessages('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 50)
  .then(messages => console.log('Messages:', messages))
  .catch(error => console.error('Error:', error));
```

**Response:**
```json
[
  {
    "id": "msg-uuid-1",
    "roomId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "senderId": "user-uuid-1",
    "senderUsername": "john_doe",
    "content": "Hello everyone!",
    "sentAt": "2026-01-09T10:05:00Z"
  },
  {
    "id": "msg-uuid-2",
    "roomId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "senderId": "user-uuid-2",
    "senderUsername": "jane_smith",
    "content": "Hi John!",
    "sentAt": "2026-01-09T10:06:00Z"
  }
]
```

## WebSocket/STOMP

### Complete JavaScript Client Example

```javascript
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class ChatClient {
  constructor(serverUrl, jwtToken) {
    this.serverUrl = serverUrl;
    this.jwtToken = jwtToken;
    this.stompClient = null;
    this.subscriptions = new Map();
  }

  connect() {
    return new Promise((resolve, reject) => {
      const socket = new SockJS(`${this.serverUrl}/ws`);
      
      this.stompClient = new Client({
        webSocketFactory: () => socket,
        connectHeaders: {
          Authorization: `Bearer ${this.jwtToken}`
        },
        debug: (str) => console.log('STOMP: ' + str),
        onConnect: () => {
          console.log('Connected to WebSocket');
          resolve();
        },
        onStompError: (frame) => {
          console.error('STOMP error:', frame);
          reject(new Error(frame.headers['message']));
        }
      });

      this.stompClient.activate();
    });
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }

  subscribeToRoom(roomId, onMessage) {
    const destination = `/topic/rooms/${roomId}`;
    
    const subscription = this.stompClient.subscribe(destination, (message) => {
      const parsedMessage = JSON.parse(message.body);
      onMessage(parsedMessage);
    });

    this.subscriptions.set(`room-${roomId}`, subscription);
    console.log(`Subscribed to room: ${roomId}`);
  }

  subscribeToTyping(roomId, onTyping) {
    const destination = `/topic/rooms/${roomId}/typing`;
    
    const subscription = this.stompClient.subscribe(destination, (message) => {
      const indicator = JSON.parse(message.body);
      onTyping(indicator);
    });

    this.subscriptions.set(`typing-${roomId}`, subscription);
    console.log(`Subscribed to typing indicators for room: ${roomId}`);
  }

  sendMessage(roomId, content) {
    this.stompClient.publish({
      destination: `/app/rooms/${roomId}/messages`,
      body: JSON.stringify({ content }),
      headers: {
        Authorization: `Bearer ${this.jwtToken}`
      }
    });
  }

  sendTypingIndicator(roomId, username, isTyping) {
    this.stompClient.publish({
      destination: `/app/rooms/${roomId}/typing`,
      body: JSON.stringify({ username, typing: isTyping }),
      headers: {
        Authorization: `Bearer ${this.jwtToken}`
      }
    });
  }

  unsubscribeFromRoom(roomId) {
    const roomSub = this.subscriptions.get(`room-${roomId}`);
    const typingSub = this.subscriptions.get(`typing-${roomId}`);
    
    if (roomSub) {
      roomSub.unsubscribe();
      this.subscriptions.delete(`room-${roomId}`);
    }
    
    if (typingSub) {
      typingSub.unsubscribe();
      this.subscriptions.delete(`typing-${roomId}`);
    }
  }
}

// Usage Example
const initializeChat = async () => {
  const jwtToken = localStorage.getItem('jwt-token');
  const client = new ChatClient('http://localhost:8080', jwtToken);

  try {
    // Connect to WebSocket
    await client.connect();

    const roomId = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890';

    // Subscribe to messages
    client.subscribeToRoom(roomId, (message) => {
      console.log('New message:', message);
      // Update UI with new message
      displayMessage(message);
    });

    // Subscribe to typing indicators
    client.subscribeToTyping(roomId, (indicator) => {
      console.log('Typing indicator:', indicator);
      // Update UI with typing status
      if (indicator.typing) {
        showTypingIndicator(indicator.username);
      } else {
        hideTypingIndicator(indicator.username);
      }
    });

    // Send a message
    document.getElementById('send-button').addEventListener('click', () => {
      const input = document.getElementById('message-input');
      client.sendMessage(roomId, input.value);
      input.value = '';
    });

    // Handle typing
    let typingTimeout;
    document.getElementById('message-input').addEventListener('input', (e) => {
      client.sendTypingIndicator(roomId, 'current-username', true);
      
      clearTimeout(typingTimeout);
      typingTimeout = setTimeout(() => {
        client.sendTypingIndicator(roomId, 'current-username', false);
      }, 1000);
    });

  } catch (error) {
    console.error('Failed to connect:', error);
  }
};

// Helper functions
function displayMessage(message) {
  const messagesDiv = document.getElementById('messages');
  const messageEl = document.createElement('div');
  messageEl.className = 'message';
  messageEl.innerHTML = `
    <strong>${message.senderUsername}</strong>
    <span class="time">${new Date(message.sentAt).toLocaleTimeString()}</span>
    <p>${message.content}</p>
  `;
  messagesDiv.appendChild(messageEl);
  messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

function showTypingIndicator(username) {
  const indicator = document.getElementById('typing-indicator');
  indicator.textContent = `${username} is typing...`;
  indicator.style.display = 'block';
}

function hideTypingIndicator(username) {
  const indicator = document.getElementById('typing-indicator');
  indicator.style.display = 'none';
}
```

### Python Client Example

```python
import asyncio
import json
from stompy import StompClient

class ChatClient:
    def __init__(self, host, port, jwt_token):
        self.host = host
        self.port = port
        self.jwt_token = jwt_token
        self.client = None
    
    async def connect(self):
        self.client = StompClient(self.host, self.port)
        await self.client.connect(headers={
            'Authorization': f'Bearer {self.jwt_token}'
        })
    
    async def subscribe_to_room(self, room_id, callback):
        destination = f'/topic/rooms/{room_id}'
        await self.client.subscribe(destination, callback)
    
    async def send_message(self, room_id, content):
        destination = f'/app/rooms/{room_id}/messages'
        await self.client.send(
            destination,
            json.dumps({'content': content}),
            headers={'Authorization': f'Bearer {self.jwt_token}'}
        )
    
    async def disconnect(self):
        if self.client:
            await self.client.disconnect()

# Usage
async def main():
    jwt_token = 'your-jwt-token-here'
    room_id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
    
    client = ChatClient('localhost', 8080, jwt_token)
    await client.connect()
    
    def on_message(message):
        data = json.loads(message.body)
        print(f"{data['senderUsername']}: {data['content']}")
    
    await client.subscribe_to_room(room_id, on_message)
    await client.send_message(room_id, 'Hello from Python!')
    
    await asyncio.sleep(60)  # Keep connection alive
    await client.disconnect()

if __name__ == '__main__':
    asyncio.run(main())
```

## Error Handling

### Common HTTP Status Codes

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data or validation error
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists (e.g., duplicate username)
- `500 Internal Server Error` - Server error

### Error Response Format

```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2026-01-09T10:00:00Z"
}
```

### Validation Errors

```json
{
  "username": "Username must be between 3 and 50 characters",
  "email": "Email must be valid",
  "password": "Password must be between 6 and 100 characters"
}
```

### Handling Errors in JavaScript

```javascript
const handleApiCall = async (apiFunction) => {
  try {
    const result = await apiFunction();
    return { success: true, data: result };
  } catch (error) {
    if (error.response) {
      // Server responded with error status
      const errorData = await error.response.json();
      return { 
        success: false, 
        error: errorData.message || 'Unknown error',
        status: error.response.status 
      };
    } else {
      // Network error or other issue
      return { 
        success: false, 
        error: 'Network error or server unavailable'
      };
    }
  }
};

// Usage
const result = await handleApiCall(() => login('john_doe', 'wrong-password'));
if (!result.success) {
  if (result.status === 401) {
    console.error('Invalid credentials');
  } else {
    console.error('Error:', result.error);
  }
}
```

## Rate Limiting and Best Practices

1. **Authentication**: Always include the JWT token in headers
2. **Message Content**: Maximum 4000 characters
3. **Username**: 3-50 characters
4. **Password**: 6-100 characters (hashed with BCrypt)
5. **Typing Indicators**: Debounce sending (recommended: 1 second)
6. **Reconnection**: Implement exponential backoff for WebSocket reconnection
7. **Token Refresh**: JWT tokens expire after 24 hours by default

## Complete Example: React Chat Component

See `docs/REACT_EXAMPLE.md` for a complete React component implementation.
