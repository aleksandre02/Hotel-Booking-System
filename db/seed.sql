-- Sample seed data for Hotel Booking System - Users and Guests only

-- Admin and regular users
INSERT INTO users (username, password_hash, role)
VALUES ('admin', 'change_me', 'ADMIN'),
       ('guest1', 'guest_pass', 'GUEST');

-- Sample guests
INSERT INTO guests (name, contact_info)
VALUES ('John Doe', 'john.doe@example.com'),
       ('Jane Smith', 'jane.smith@example.com'),
       ('Bob Johnson', 'bob.johnson@example.com'),
       ('Alice Williams', 'alice.williams@example.com');
