package org.example.model;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String username;
    protected String password;
    protected String role; // "user" or "admin"
    protected List<String> readingHistory;
    protected Map<String, Integer> preferences;
    protected Map<String, Integer> ratings; // Article ID -> Rating

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.readingHistory = new ArrayList<>();
        this.preferences = new HashMap<>();
        this.ratings = new HashMap<>();
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public List<String> getReadingHistory() {
        return readingHistory;
    }

    public Map<String, Integer> getPreferences() {
        return preferences;
    }

    public Map<String, Integer> getRatings() {
        return ratings;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
