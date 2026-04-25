# Hotel-Booking-System
The goal of this project is to develop a simple hotel reservation system that allows managing rooms, guests, and bookings. The system helps organize room availability efficiently and provides an easy reservation process.

Database setup is implemented in `src/main/java/com/hotel/db`.
Use `db/schema.sql` to create tables and `db/seed.sql` to populate sample data.
Run `DatabaseSetup` with a JDBC URL to initialize the database file.

## Database setup
1. Compile the Java sources:
   ```bash
   javac -d out src/main/java/com/hotel/auth/*.java src/main/java/com/hotel/db/*.java
   ```
2. Initialize the database file with the default SQLite path:
   ```bash
   java -cp out:PATH/TO/sqlite-jdbc.jar com.hotel.db.DatabaseSetup
   ```
3. Seed sample data by applying `db/seed.sql` with the same JDBC driver or a SQLite client.

> Note: `DatabaseSetup` defaults to `jdbc:sqlite:db/hotel.db` and uses `db/schema.sql` unless alternate paths are provided.
