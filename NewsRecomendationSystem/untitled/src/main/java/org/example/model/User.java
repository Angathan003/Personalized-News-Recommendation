package org.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private static final long serialVersionUID = 1L; // Optional but recommended

    private String username;
    private String password;
    private Map<String, Integer> preferences; // Changed from List to Map
    private List<String> readingHistory;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.preferences = new HashMap<>();
        this.readingHistory = new ArrayList<>();
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    // For security reasons, you might avoid providing a getPassword method

    public Map<String, Integer> getPreferences() {
        return preferences;
    }

    public List<String> getReadingHistory() {
        return readingHistory;
    }
}
