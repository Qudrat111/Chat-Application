CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_email ON users(email);

CREATE TABLE IF NOT EXISTS rooms (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    created_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_created_by ON rooms(created_by);

CREATE TABLE IF NOT EXISTS memberships (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    room_id UUID NOT NULL,
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    UNIQUE(user_id, room_id)
);

CREATE INDEX idx_user_id ON memberships(user_id);
CREATE INDEX idx_room_id ON memberships(room_id);
CREATE INDEX idx_user_room ON memberships(user_id, room_id);

CREATE TABLE IF NOT EXISTS messages (
    id UUID PRIMARY KEY,
    room_id UUID NOT NULL,
    sender_id UUID NOT NULL,
    content VARCHAR(4000) NOT NULL,
    sent_at TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_room_id_sent_at ON messages(room_id, sent_at);
CREATE INDEX idx_sender_id ON messages(sender_id);
