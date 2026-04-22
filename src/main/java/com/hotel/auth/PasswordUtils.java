package com.hotel.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for password hashing and verification.
 */
public class PasswordUtils {

    /**
     * Hashes a plain text password using SHA-256.
     * @param plainText the plain text password
     * @return the hashed password as a hex string
     */
    public static String hashPassword(String plainText) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plainText.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies a plain text password against a hash.
     * @param plainText the plain text password
     * @param hash the hashed password
     * @return true if they match, false otherwise
     */
    public static boolean verifyPassword(String plainText, String hash) {
        return hashPassword(plainText).equals(hash);
    }
}