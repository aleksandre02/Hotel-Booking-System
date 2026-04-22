package com.hotel.auth;

/**
 * Manual test for AuthService.
 */
public class AuthServiceTest {
    public static void main(String[] args) {
        IAuthService authService = new AuthService();

        try {
            // Register a new user
            User user = authService.register("admin", "password", Role.ADMIN);
            System.out.println("Registered: " + user);

            // Try duplicate registration
            try {
                authService.register("admin", "password", Role.ADMIN);
            } catch (AuthException e) {
                System.out.println("Expected error: " + e.getMessage());
            }

            // Login with wrong password
            try {
                authService.login("admin", "wrong");
            } catch (AuthException e) {
                System.out.println("Expected error: " + e.getMessage());
            }

            // Login correctly
            User loggedIn = authService.login("admin", "password");
            System.out.println("Logged in: " + loggedIn);

            // Check role
            System.out.println("Is admin: " + authService.isAdmin());

            // Logout
            authService.logout();
            System.out.println("Logged out");

            // Check session is cleared
            System.out.println("Is logged in: " + authService.isLoggedIn());

        } catch (AuthException e) {
            e.printStackTrace();
        }
    }
}