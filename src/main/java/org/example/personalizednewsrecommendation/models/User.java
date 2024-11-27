package org.example.personalizednewsrecommendation.models;

public class User {
    private String username;
    private String password;
    private String preferences;

    public User(String username, String password, String preferences) {
        this.username = username;
        this.password = password;
        this.preferences = preferences;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getPreferences() { return preferences; }
}
