package com.hotel.auth;

/**
 * Custom exception for authentication errors.
 */
public class AuthException extends Exception {

    /**
     * Constructs a new AuthException with the specified message.
     * @param message the error message
     */
    public AuthException(String message) {
        super(message);
    }
}