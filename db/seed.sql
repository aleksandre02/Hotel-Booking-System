-- Sample seed data for Hotel Booking System

INSERT INTO users (username, password_hash, role)
VALUES ('admin', 'change_me', 'ADMIN');

INSERT INTO guests (name, contact_info)
VALUES ('John Doe', 'john.doe@example.com');

INSERT INTO rooms (type, price, is_available)
VALUES ('SingleRoom', 75.00, 1),
       ('DoubleRoom', 120.00, 1),
       ('Suite', 210.00, 1);

INSERT INTO reservations (guest_id, room_id, start_date, end_date, status)
VALUES (1, 1, '2026-05-01', '2026-05-05', 'ACTIVE');
