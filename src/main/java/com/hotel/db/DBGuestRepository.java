package com.hotel.db;

import com.hotel.auth.Guest;
import com.hotel.auth.IGuestRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBGuestRepository implements IGuestRepository {
    private final DatabaseManager databaseManager;

    public DBGuestRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void addGuest(Guest guest) {
        String sql = "INSERT INTO guests(user_id, name, contact_info) VALUES (?, ?, ?)";
        try {
            Connection connection = databaseManager.connect();
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, guest.getUserId());
                statement.setString(2, guest.getName());
                statement.setString(3, guest.getContactInfo());
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        guest.setGuestId(keys.getInt(1));
                    }
                }
            }
            connection.commit();
        } catch (DBException | SQLException e) {
            databaseManager.rollback();
            throw new RuntimeException("Failed to add guest", e);
        }
    }

    @Override
    public Guest getGuestById(int id) {
        String sql = "SELECT guest_id, user_id, name, contact_info FROM guests WHERE guest_id = ?";
        try {
            Connection connection = databaseManager.connect();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return new Guest(
                            rs.getInt("guest_id"),
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("contact_info")
                        );
                    }
                    return null;
                }
            }
        } catch (DBException | SQLException e) {
            throw new RuntimeException("Failed to load guest by id", e);
        }
    }

    @Override
    public List<Guest> getAllGuests() {
        String sql = "SELECT guest_id, user_id, name, contact_info FROM guests";
        try {
            Connection connection = databaseManager.connect();
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {
                List<Guest> guests = new ArrayList<>();
                while (rs.next()) {
                    guests.add(new Guest(
                        rs.getInt("guest_id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("contact_info")
                    ));
                }
                return guests;
            }
        } catch (DBException | SQLException e) {
            throw new RuntimeException("Failed to load all guests", e);
        }
    }
}
