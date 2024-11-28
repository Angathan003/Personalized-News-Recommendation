package org.example.personalizednewsrecommendation.services;

import org.example.personalizednewsrecommendation.models.User;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final Map<String, User> users = new HashMap<>();

    public UserManager() {
        users.put("testuser", new User("testuser", "password123", "Technology"));
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public void register(String username, String password, String preferences) {
        users.put(username, new User(username, password, preferences));
    }

    public User getUser(String username) {
        return users.get(username);
    }
}
