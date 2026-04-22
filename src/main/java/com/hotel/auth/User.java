package com.hotel.auth;

/**
 * Represents a user in the system.
 */
public class User {
    private final int userId;
    private final String username;
    private final String passwordHash;
    private final Role role;

    /**
     * Constructs a new User.
     * @param userId the user ID
     * @param username the username
     * @param passwordHash the hashed password
     * @param role the user's role
     */
    public User(int userId, String username, String passwordHash, Role role) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    /**
     * Gets the user ID.
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets the username.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password hash.
     * @return the password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Gets the user's role.
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{id=" + userId + ", username='" + username + "', role=" + role + "}";
    }
}