package com.hotel.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager implements AutoCloseable {
    private final String jdbcUrl;
    private final String schemaFilePath;
    private Connection connection;

    public DatabaseManager(String jdbcUrl, String schemaFilePath) {
        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            throw new IllegalArgumentException("jdbcUrl is required");
        }
        if (schemaFilePath == null || schemaFilePath.isBlank()) {
            throw new IllegalArgumentException("schemaFilePath is required");
        }
        this.jdbcUrl = jdbcUrl;
        this.schemaFilePath = schemaFilePath;
    }

    public Connection connect() throws DBException {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(jdbcUrl);
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                throw new DBException("Unable to open database connection", e);
            }
        }
        return connection;
    }

    public void initializeDatabase() throws DBException {
        connect();
        executeSqlFile(schemaFilePath);
    }

    public void executeSqlFile(String filePath) throws DBException {
        connect();
        String sql = readSqlFile(filePath);
        try (Statement statement = connection.createStatement()) {
            for (String script : splitSqlStatements(sql)) {
                if (script.isBlank()) {
                    continue;
                }
                statement.execute(script);
            }
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new DBException("Failed to execute SQL file: " + filePath, e);
        }
    }

    private static String readSqlFile(String filePath) throws DBException {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new DBException("Unable to read SQL file: " + filePath, e);
        }
    }

    private static String[] splitSqlStatements(String sql) {
        return sql.split("(?m);\\s*(?:\\r?\\n|$)");
    }

    public void rollback() {
        if (connection == null) {
            return;
        }
        try {
            connection.rollback();
        } catch (SQLException ignored) {
            // ignore rollback failure
        }
    }

    @Override
    public void close() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException ignored) {
            // ignore close failure
        }
    }
}
