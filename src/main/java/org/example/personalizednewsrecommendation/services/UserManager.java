package org.example.personalizednewsrecommendation.services;

import org.example.personalizednewsrecommendation.models.Admin;
import org.example.personalizednewsrecommendation.models.User;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private final Map<String, User> users = new HashMap<>();
    private final Admin admin = new Admin();

    public boolean login(String username, String password, String role) {
        if (role.equals("Admin")) {
            return admin.getUsername().equals(username) && admin.getPassword().equals(password);
        } else {
            User user = users.get(username);
            return user != null && user.getPassword().equals(password);
        }
    }
}
