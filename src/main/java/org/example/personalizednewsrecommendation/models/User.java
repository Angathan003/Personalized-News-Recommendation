package org.example.personalizednewsrecommendation.models;

public class User {
    private String username;
    private String password;
    private String role;
    private String preferences; // Additional field

    // Constructor
    public User(String username, String password, String role, String preferences) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.preferences = preferences; // Initialize preferences
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
