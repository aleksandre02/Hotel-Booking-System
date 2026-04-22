package com.hotel.auth;

/**
 * Interface for authentication services.
 */
public interface IAuthService {

    /**
     * Logs in a user with username and password.
     * @param username the username
     * @param password the password
     * @return the logged-in user
     * @throws AuthException if login fails
     */
    User login(String username, String password) throws AuthException;

    /**
     * Registers a new user.
     * @param username the username
     * @param password the password
     * @param role the role
     * @return the registered user
     * @throws AuthException if registration fails
     */
    User register(String username, String password, Role role) throws AuthException;

    /**
     * Logs out the current user.
     */
    void logout();

    /**
     * Gets the current logged-in user.
     * @return the current user or null if not logged in
     */
    User getCurrentUser();

    /**
     * Checks if a user is logged in.
     * @return true if logged in, false otherwise
     */
    boolean isLoggedIn();

    /**
     * Checks if the current user is an admin.
     * @return true if admin, false otherwise
     */
    boolean isAdmin();
}