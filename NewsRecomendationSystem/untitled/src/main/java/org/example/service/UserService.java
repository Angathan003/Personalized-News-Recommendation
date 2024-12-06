package org.example.service;

import org.example.model.User;
import org.example.util.FileHandler;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private Map<String, User> userDatabase;
    private FileHandler fileHandler;

    public UserService() {
        this.userDatabase = new HashMap<>();
        this.fileHandler = new FileHandler();
        loadUsers();
    }

    private void loadUsers() {
        // Load users from file
        userDatabase = fileHandler.readUsersFromFile();
    }

    public boolean createAccount(String username, String password) {
        if (userDatabase.containsKey(username)) {
            System.out.println("User already exists.");
            return false;
        }
        User user = new User(username, password);
        userDatabase.put(username, user);
        fileHandler.writeUsersToFile(userDatabase);
        System.out.println("Account created successfully.");
        return true;
    }

    public User login(String username, String password) {
        User user = userDatabase.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful.");
            return user;
        }
        System.out.println("Invalid username or password.");
        return null;
    }

    public void updatePreferences(User user, String category) {
        Map<String, Integer> preferences = user.getPreferences();
        int newCount = preferences.getOrDefault(category, 0) + 1;
        preferences.put(category, newCount);
        fileHandler.writeUsersToFile(userDatabase);
        System.out.println("Updated preferences: " + category + " count is now " + newCount);
    }


    public void addToReadingHistory(User user, String articleId) {
        if (!user.getReadingHistory().contains(articleId)) {
            user.getReadingHistory().add(articleId);
            fileHandler.writeUsersToFile(userDatabase);
        }
    }
}
