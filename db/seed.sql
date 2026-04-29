-- Sample seed data for Hotel Booking System - Users and Guests only

-- Admin and regular users
INSERT INTO users (username, password_hash, role)
VALUES ('admin', 'change_me', 'ADMIN'),
       ('guest1', 'guest_pass', 'GUEST'),
       ('guest2', 'guest_pass', 'GUEST'),
       ('guest3', 'guest_pass', 'GUEST'),
       ('guest4', 'guest_pass', 'GUEST');

-- Sample guests linked to user accounts
INSERT INTO guests (user_id, name, contact_info)
VALUES (2, 'John Doe', 'john.doe@example.com'),
       (3, 'Jane Smith', 'jane.smith@example.com'),
       (4, 'Bob Johnson', 'bob.johnson@example.com'),
       (5, 'Alice Williams', 'alice.williams@example.com');
