package com.hotel.db;

import com.hotel.room.DoubleRoom;
import com.hotel.room.Room;
import com.hotel.room.RoomStatus;
import com.hotel.room.SingleRoom;
import com.hotel.room.Suite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository {
    private static final String JDBC_URL = "jdbc:sqlite:db/hotel.db";

    public RoomRepository() {
        createTableIfMissing();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    private void createTableIfMissing() {
        String sql = """
                CREATE TABLE IF NOT EXISTS rooms (
                    room_id INTEGER PRIMARY KEY,
                    type TEXT NOT NULL,
                    price REAL NOT NULL,
                    status TEXT NOT NULL
                )
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Could not create rooms table", e);
        }
    }

    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT room_id, type, price, status FROM rooms ORDER BY room_id";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int roomId = resultSet.getInt("room_id");
                String type = resultSet.getString("type");
                double price = resultSet.getDouble("price");
                RoomStatus status = RoomStatus.valueOf(resultSet.getString("status"));

                rooms.add(createRoom(roomId, type, price, status));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not load rooms", e);
        }

        return rooms;
    }

    public void save(Room room) {
        String sql = """
                INSERT INTO rooms (room_id, type, price, status)
                VALUES (?, ?, ?, ?)
                ON CONFLICT(room_id) DO UPDATE SET
                    type = excluded.type,
                    price = excluded.price,
                    status = excluded.status
                """;

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, room.getRoomId());
            statement.setString(2, room.getType());
            statement.setDouble(3, room.getPrice());
            statement.setString(4, room.getStatus().name());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not save room", e);
        }
    }

    public void saveAll(List<Room> rooms) {
        for (Room room : rooms) {
            save(room);
        }
    }

    public void updateStatus(int roomId, RoomStatus status) {
        String sql = "UPDATE rooms SET status = ? WHERE room_id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status.name());
            statement.setInt(2, roomId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not update room status", e);
        }
    }

    private Room createRoom(int roomId, String type, double price, RoomStatus status) {
        if ("Single".equalsIgnoreCase(type)) {
            return new SingleRoom(roomId, price, status);
        }

        if ("Double".equalsIgnoreCase(type)) {
            return new DoubleRoom(roomId, price, status);
        }

        return new Suite(roomId, price, status, false);
    }
}
