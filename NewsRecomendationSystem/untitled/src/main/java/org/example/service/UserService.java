package org.example.service;

import org.example.exceptions.AuthenticationException;
import org.example.exceptions.InvalidDataException;
import org.example.exceptions.UnauthorizedActionException;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.model.Admin;
import org.example.model.User;
import org.example.util.FileHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserService {
    protected Map<String, User> userDatabase;
    protected FileHandler fileHandler;

    // Removed USERNAME_PATTERN as username is now the email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$%^&+=]{6,}$"); // Updated to allow certain special characters

    public UserService() {
        this.fileHandler = new FileHandler();
        this.userDatabase = new HashMap<>();
        loadUsers();

        // Ensure the admin user exists
        if (!userDatabase.containsKey("admin")) {
            Admin adminUser = new Admin("admin", "admin123");
            userDatabase.put(adminUser.getUsername(), adminUser);
            try {
                fileHandler.writeUsersToFile(userDatabase);
                System.out.println("Default admin account created: username='admin', password='admin123'");
            } catch (IOException e) {
                System.out.println("Failed to create default admin account: " + e.getMessage());
            }
        }
    }

    /**
     * Loads users from persistent storage into the userDatabase map.
     */
    private void loadUsers() {
        try {
            Map<String, User> usersFromFile = fileHandler.readUsersFromFile();
            if (usersFromFile != null) {
                userDatabase.putAll(usersFromFile);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load users from file: " + e.getMessage());
        }
    }

    /**
     * Creates a new regular user account after validating input data.
     *
     * @param username The desired username (email format).
     * @param password The desired password.
     * @param role     The role of the user ("user" only).
     * @throws InvalidDataException         If input data is invalid.
     * @throws UserAlreadyExistsException   If the username is already taken.
     * @throws UnauthorizedActionException  If the role assignment is unauthorized.
     */
    public void createAccount(String username, String password, String role)
            throws InvalidDataException, UserAlreadyExistsException, UnauthorizedActionException {
        // Validate email and password
        validateEmail(username);
        validatePassword(password);

        // Prevent unauthorized role assignments
        if (!role.equalsIgnoreCase("user")) {
            throw new UnauthorizedActionException("Invalid role specified. Only 'user' role can be created through this method.");
        }

        if (userDatabase.containsKey(username)) {
            throw new UserAlreadyExistsException("Username '" + username + "' is already taken.");
        }

        User newUser = new User(username, password, "user");
        userDatabase.put(username, newUser);
        try {
            fileHandler.writeUsersToFile(userDatabase);
            System.out.println("User account created successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save user data: " + e.getMessage());
        }
    }

    /**
     * Creates a new admin account. This method should be restricted and not exposed through regular user flows.
     *
     * @param username The desired admin username (email format).
     * @param password The desired admin password.
     * @throws InvalidDataException         If input data is invalid.
     * @throws UserAlreadyExistsException   If the username is already taken.
     */
    public void createAdminAccount(String username, String password)
            throws InvalidDataException, UserAlreadyExistsException {
        // Validate input data
        validateEmail(username);
        validatePassword(password);

        if (userDatabase.containsKey(username)) {
            throw new UserAlreadyExistsException("Username '" + username + "' is already taken.");
        }

        Admin adminUser = new Admin(username, password);
        userDatabase.put(adminUser.getUsername(), adminUser);
        try {
            fileHandler.writeUsersToFile(userDatabase);
            System.out.println("Admin account created successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save admin data: " + e.getMessage());
        }
    }

    /**
     * Authenticates a user or admin with provided credentials.
     *
     * @param username The username.
     * @param password The password.
     * @return The authenticated User or Admin object.
     * @throws AuthenticationException If authentication fails.
     */
    public User login(String username, String password) throws AuthenticationException {
        User user = userDatabase.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful.");
            return user;
        }
        throw new AuthenticationException("Invalid username or password.");
    }

    /**
     * Updates user preferences based on the article category.
     *
     * @param user     The current user.
     * @param category The category to update.
     */
    public void updatePreferences(User user, String category) {
        Map<String, Integer> preferences = user.getPreferences();
        int newCount = preferences.getOrDefault(category, 0) + 1;
        preferences.put(category, newCount);
        try {
            fileHandler.writeUsersToFile(userDatabase);
            System.out.println("Updated preferences: " + category + " count is now " + newCount);
        } catch (IOException e) {
            System.out.println("Failed to update preferences: " + e.getMessage());
        }
    }

    /**
     * Adds an article to the user's reading history.
     *
     * @param user      The current user.
     * @param articleId The ID of the article.
     */
    public void addToReadingHistory(User user, String articleId) {
        if (!user.getReadingHistory().contains(articleId)) {
            user.getReadingHistory().add(articleId);
            try {
                fileHandler.writeUsersToFile(userDatabase);
            } catch (IOException e) {
                System.out.println("Failed to update reading history: " + e.getMessage());
            }
        }
    }

    /**
     * Records a user's rating for an article.
     *
     * @param user      The current user.
     * @param articleId The ID of the article.
     * @param rating    The rating value.
     */
    public void rateArticle(User user, String articleId, int rating) {
        user.getRatings().put(articleId, rating);
        try {
            fileHandler.writeUsersToFile(userDatabase);
        } catch (IOException e) {
            System.out.println("Failed to record rating: " + e.getMessage());
        }
    }

    /**
     * Updates the user's username after validation.
     *
     * @param user        The current user.
     * @param newUsername The new desired username.
     * @return true if update is successful, false if username is taken.
     * @throws InvalidDataException       If the new username is invalid.
     * @throws UserAlreadyExistsException If the new username is already taken.
     */
    public boolean updateUsername(User user, String newUsername)
            throws InvalidDataException, UserAlreadyExistsException {
        // Since username is an email, validate it
        validateEmail(newUsername);

        if (userDatabase.containsKey(newUsername)) {
            throw new UserAlreadyExistsException("Username '" + newUsername + "' is already taken.");
        }

        userDatabase.remove(user.getUsername());
        user.setUsername(newUsername);
        userDatabase.put(newUsername, user);
        try {
            fileHandler.writeUsersToFile(userDatabase);
            return true;
        } catch (IOException e) {
            System.out.println("Failed to update username: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the user's password after validation.
     *
     * @param user        The current user.
     * @param newPassword The new desired password.
     * @throws InvalidDataException If the new password is invalid.
     */
    public void updatePassword(User user, String newPassword) throws InvalidDataException {
        validatePassword(newPassword);
        user.setPassword(newPassword);
        try {
            fileHandler.writeUsersToFile(userDatabase);
            System.out.println("Password updated successfully.");
        } catch (IOException e) {
            System.out.println("Failed to update password: " + e.getMessage());
        }
    }

    /**
     * Validates the email format.
     *
     * @param email The email to validate.
     * @throws InvalidDataException If the email format is invalid.
     */
    private void validateEmail(String email) throws InvalidDataException {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidDataException("Invalid email format.");
        }
    }

    /**
     * Validates the password against defined patterns.
     *
     * @param password The password to validate.
     * @throws InvalidDataException If the password is invalid.
     */
    private void validatePassword(String password) throws InvalidDataException {
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidDataException("Invalid password. It should be at least 6 characters long and include both letters and numbers.");
        }
    }
    public Map<String, User> getUserDatabase() {
        return userDatabase;
    }
}
