package org.example.personalizednewsrecommendation.services;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
