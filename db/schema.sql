-- Hotel Booking System schema based on the class diagram
-- Tables: users, guests, rooms, reservations

-- Users table corresponds to the User class
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    role TEXT NOT NULL
);

-- Guests table corresponds to the Guest class
CREATE TABLE IF NOT EXISTS guests (
    guest_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    contact_info TEXT
);

-- Rooms table corresponds to the Room class and stores room subtype in the type column
CREATE TABLE IF NOT EXISTS rooms (
    room_id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL,
    price REAL NOT NULL,
    is_available INTEGER NOT NULL DEFAULT 1 -- 1 = true, 0 = false
);

-- Reservations table corresponds to the Reservation class
CREATE TABLE IF NOT EXISTS reservations (
    reservation_id INTEGER PRIMARY KEY AUTOINCREMENT,
    guest_id INTEGER NOT NULL,
    room_id INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status TEXT NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guest_id) REFERENCES guests(guest_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

-- Example room types: 'SingleRoom', 'DoubleRoom', 'Suite'
-- The Room subclasses from the diagram are represented using the type column above.

-- Seed data is kept in db/seed.sql so schema creation remains idempotent.
-- Run db/seed.sql separately after the schema has been created.
