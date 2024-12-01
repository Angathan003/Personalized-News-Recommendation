package org.example.personalizednewsrecommendation.services;

import org.example.personalizednewsrecommendation.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12748920";
    private static final String DB_USER = "sql12748920";
    private static final String DB_PASSWORD = "uBBZknHp1V";

    // Establish a connection to the database
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Save a user to the database
    public void saveUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, password, role, preferences) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getPreferences());
            stmt.executeUpdate();
        }
    }

    // Retrieve a user from the database
    public User getUser(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("preferences")
                );
            }
        }
        return null;
    }


    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("preferences")
                ));
            }
        }
        return users;
    }












    // Update user preferences in the database
    public void updateUserPreferences(String username, String preferences) throws SQLException {
        String query = "UPDATE users SET preferences = ? WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, preferences);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    // Authenticate a user
    public boolean authenticateUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
