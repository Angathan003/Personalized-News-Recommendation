package org.example.personalizednewsrecommendation.services;

import org.example.personalizednewsrecommendation.models.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final Map<String, User> users = new HashMap<>();
    private final DatabaseManager databaseManager;

    public UserManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        // Load users from database and populate the map
        try {
            for (User user : databaseManager.getAllUsers()) {
                users.put(user.getUsername(), user);
            }
        } catch (SQLException e) {
            System.err.println("Error loading users from the database: " + e.getMessage());
        }
    }


    public void updateUserPreferences(String username, String preferences) {
        User user = users.get(username);
        if (user != null) {
            user.setPreferences(preferences); // Update the preferences locally
            try {
                databaseManager.updateUserPreferences(username, preferences); // Update in DB
            } catch (SQLException e) {
                System.err.println("Error updating user preferences in the database: " + e.getMessage());
            }
        }
    }

    public String getUserPreferences(String username) {
        User user = users.get(username);
        return user != null ? user.getPreferences() : "";
    }

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println(username);
            return false; // Username already exists

        }

        User newUser = new User(username, password, "user", "");
        try {
            databaseManager.saveUser(newUser); // Save the user to the database
            users.put(username, newUser); // Add to local cache
            return true;
        } catch (SQLException e) {
            System.err.println("Error registering user in the database: " + e.getMessage());
            return false;
        }
    }

    public boolean authenticateUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public boolean authenticateAdmin(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password) && "admin".equals(user.getRole());
    }

    public User getUser(String username) {
        return users.get(username);
    }
}
