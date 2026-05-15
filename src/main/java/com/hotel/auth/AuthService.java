package com.hotel.auth;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of authentication service with in-memory storage.
 */
public class AuthService implements IAuthService {
    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    private final List<User> users = new ArrayList<>();
    private int nextUserId = 1;
    private final SessionManager sessionManager = SessionManager.getInstance();

    public AuthService() {
        try {
            register(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD, Role.ADMIN);
        } catch (AuthException ignored) {
            // Default admin already exists or registration failed with an expected duplicate.
        }
    }

    @Override
    public User login(String username, String password) throws AuthException {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
                    sessionManager.setCurrentUser(user);
                    return user;
                } else {
                    throw new AuthException("Invalid credentials");
                }
            }
        }
        throw new AuthException("Invalid credentials");
    }

    @Override
    public User register(String username, String password, Role role) throws AuthException {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                throw new AuthException("Username already exists");
            }
        }
        String hash = PasswordUtils.hashPassword(password);
        User newUser = new User(nextUserId++, username, hash, role);
        users.add(newUser);
        return newUser;
    }

    @Override
    public void logout() {
        sessionManager.clearSession();
    }

    @Override
    public User getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    @Override
    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    @Override
    public boolean isAdmin() {
        User user = sessionManager.getCurrentUser();
        return user != null && user.getRole() == Role.ADMIN;
    }
}