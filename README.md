# Hotel-Booking-System

A simple hotel reservation system demonstrating object-oriented programming principles.

### Database Layer Implementation

This implementation provides the database schema, persistent data storage, and CRUD operations for the Hotel Booking System.

### Scope

**Database Layer** (Person 4 responsibility):
- Schema design with referential integrity
- CRUD operations for users and guests
- Data consistency checks
- Database initialization

### Architecture

```
src/main/java/com/hotel/
├── auth/              # Existing auth entities
│   ├── User.java
│   ├── Role.java
│   ├── AuthService.java
│   ├── Guest.java
│   ├── GuestService.java
│   └── ... (other auth classes)
└── db/                # Database Layer
    ├── DatabaseManager.java      # Connection & SQL execution
    ├── DBException.java          # Database exceptions
    ├── DBUserRepository.java      # User CRUD
    ├── DBGuestRepository.java     # Guest CRUD
    └── DatabaseSetup.java        # CLI initialization
```

### Database Schema

**Tables (created by db/schema.sql)**
- **users**: System user credentials and roles
- **guests**: Guest information (name, contact)
- **rooms**: Room inventory
- **reservations**: Booking records

**Indexes**: FK constraints and query optimization indexes

### CRUD Operations

**DBUserRepository**:
- `addUser(user)` — Create
- `getUserById(id)` — Read by ID
- `getUserByUsername(username)` — Read by username
- `getAllUsers()` — Read all
- `deleteUser(id)` — Delete

**DBGuestRepository**:
- `addGuest(guest)` — Create
- `getGuestById(id)` — Read by ID
- `getAllGuests()` — Read all

### Data Integrity Features

- ✅ Foreign key constraints (guests → users potential, reservations → guests/rooms)
- ✅ Transaction support with rollback
- ✅ Unique constraints (username)
- ✅ NOT NULL constraints on required fields
- ✅ Auto-increment primary keys
- ✅ Date storage in standard format

### Setup & Compilation

#### 1. Compile database classes
```bash
javac -d out \
  src/main/java/com/hotel/auth/{User,Role,Guest}.java \
  src/main/java/com/hotel/db/*.java
```

#### 2. Initialize database
```bash
java -cp out com.hotel.db.DatabaseSetup
```

This creates `db/hotel.db` (SQLite) using `db/schema.sql`.

#### 3. Seed sample data (optional)
```bash
sqlite3 db/hotel.db < db/seed.sql
```

### Database Connection

**Default Configuration**:
- Type: SQLite
- File: `db/hotel.db`
- Schema: `db/schema.sql`

**Custom Configuration**:
```bash
java -cp out com.hotel.db.DatabaseSetup "jdbc:sqlite:custom/path.db" "custom/schema.sql"
```

### Usage Example

```java
import com.hotel.db.DatabaseManager;
import com.hotel.db.DBUserRepository;
import com.hotel.db.DBGuestRepository;
import com.hotel.auth.User;
import com.hotel.auth.Role;
import com.hotel.auth.Guest;

// Initialize database
DatabaseManager dbManager = new DatabaseManager(
    "jdbc:sqlite:db/hotel.db",
    "db/schema.sql"
);
dbManager.initializeDatabase();

// User CRUD
DBUserRepository userRepo = new DBUserRepository(dbManager);
User newUser = new User(0, "john_admin", "hashed_pwd", Role.ADMIN);
userRepo.addUser(newUser);

User user = userRepo.getUserByUsername("john_admin");
System.out.println("User: " + user);

// Guest CRUD  
DBGuestRepository guestRepo = new DBGuestRepository(dbManager);
Guest guest = new Guest(0, "John Doe", "john@example.com");
guestRepo.addGuest(guest);

List<Guest> allGuests = guestRepo.getAllGuests();

dbManager.close();
```

### Notes

- Database operations are transactional with auto-commit disabled
- Invalid SQL is caught and wrapped in `DBException`
- Connection lifecycle managed by `DatabaseManager`
- JDBC driver required: SQLite JDBC (if using sqlite)
- Other team members will extend this with Room, Reservation, and Hotel controller classes
