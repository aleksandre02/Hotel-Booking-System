package com.hotel.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatabaseSetup {
    public static void main(String[] args) {
        String jdbcUrl = args.length > 0 ? args[0] : "jdbc:sqlite:db/hotel.db";
        String schemaPath = args.length > 1 ? args[1] : "db/schema.sql";

        try {
            Path dbDirectory = Path.of("db");
            if (Files.notExists(dbDirectory)) {
                Files.createDirectories(dbDirectory);
            }

            try (DatabaseManager databaseManager = new DatabaseManager(jdbcUrl, schemaPath)) {
                databaseManager.initializeDatabase();
                System.out.println("Database initialized successfully: " + jdbcUrl);
            }
        } catch (IOException e) {
            System.err.println("Unable to create database directory: " + e.getMessage());
            System.exit(1);
        } catch (DBException e) {
            System.err.println("Database setup failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
