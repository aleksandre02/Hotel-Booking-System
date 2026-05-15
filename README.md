# Hotel Booking System

A Java Object-Oriented Programming project for managing hotel rooms, guests, and reservations.  
The system includes a Swing graphical user interface, role-based access control, room management, booking functionality, and SQLite-based room persistence.

---

## Project Overview

The Hotel Booking System allows guests to view rooms, create reservations, and manage their own bookings.  
Administrators can manage hotel rooms, update room details, and search reservations.

The project demonstrates core OOP principles such as:

- Encapsulation
- Abstraction
- Inheritance
- Polymorphism
- Association
- Aggregation / Composition

---

## Features

### Guest Features

Guests can:

- Register an account
- Log in
- View all rooms
- View available rooms
- Filter rooms by type and price
- Create reservations
- View their own reservations
- Cancel their own reservations

### Admin Features

Admins can:

- Log in with admin credentials
- View all rooms
- Add new rooms
- Update existing room details
- View all reservations
- Search reservations
- Cancel reservations
- Manage room availability

---

## User Roles

The system supports two roles:

| Role | Description |
|---|---|
| `GUEST` | Regular user who can book rooms and manage their own reservations |
| `ADMIN` | Staff/admin user who can manage rooms and view/search reservations |

Normal users are registered as `GUEST` by default.  
Admin access is protected using role checks.

---

## Default Admin Login

Use the following default admin account:

```text
Username: admin
Password: admin123
