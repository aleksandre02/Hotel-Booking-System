package com.hotel.auth;

/**
 * Singleton class for managing user sessions.
 */
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    /**
     * Gets the singleton instance.
     * @return the SessionManager instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Sets the current user.
     * @param user the user to set
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Gets the current user.
     * @return the current user or null
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Clears the session.
     */
    public void clearSession() {
        this.currentUser = null;
    }

    /**
     * Checks if a user is logged in.
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}