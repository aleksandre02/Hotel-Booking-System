package com.hotel.db;

import com.hotel.auth.User;
import com.hotel.auth.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBUserRepository {
    private final DatabaseManager databaseManager;

    public DBUserRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void addUser(User user) {
        String sql = "INSERT INTO users(username, password_hash, role) VALUES (?, ?, ?)";
        try {
            Connection connection = databaseManager.connect();
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPasswordHash());
                statement.setString(3, user.getRole().toString());
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        // Note: User class stores userId, but we cannot set it after construction
                        // This is a limitation of the current User class design
                    }
                }
            }
            connection.commit();
        } catch (DBException | SQLException e) {
            databaseManager.rollback();
            throw new RuntimeException("Failed to add user", e);
        }
    }

    public User getUserById(int id) {
        String sql = "SELECT user_id, username, password_hash, role FROM users WHERE user_id = ?";
        try {
            Connection connection = databaseManager.connect();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            Role.valueOf(rs.getString("role"))
                        );
                    }
                    return null;
                }
            }
        } catch (DBException | SQLException e) {
            throw new RuntimeException("Failed to load user by id", e);
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT user_id, username, password_hash, role FROM users WHERE username = ?";
        try {
            Connection connection = databaseManager.connect();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            Role.valueOf(rs.getString("role"))
                        );
                    }
                    return null;
                }
            }
        } catch (DBException | SQLException e) {
            throw new RuntimeException("Failed to load user by username", e);
        }
    }

    public List<User> getAllUsers() {
        String sql = "SELECT user_id, username, password_hash, role FROM users";
        try {
            Connection connection = databaseManager.connect();
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        Role.valueOf(rs.getString("role"))
                    ));
                }
                return users;
            }
        } catch (DBException | SQLException e) {
            throw new RuntimeException("Failed to load all users", e);
        }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try {
            Connection connection = databaseManager.connect();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
            connection.commit();
        } catch (DBException | SQLException e) {
            databaseManager.rollback();
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}
