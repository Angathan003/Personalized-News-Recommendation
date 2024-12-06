package org.example;

import org.example.exceptions.AuthenticationException;
import org.example.exceptions.InvalidDataException;
import org.example.exceptions.UnauthorizedActionException;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.model.User;
import org.example.service.UserService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrencyTest {
    public static void main(String[] args) {
        UserService userService = new UserService();

        // Add test users
        try {
            userService.createAccount("user1@example.com", "Password123", "user");
            userService.createAccount("user2@example.com", "Password123", "user");
            userService.createAccount("user3@example.com", "Password123", "user");
        } catch (InvalidDataException | UserAlreadyExistsException | UnauthorizedActionException e) {
            System.out.println("Failed to create test users: " + e.getMessage());
            return;
        }

        // Executor to simulate concurrent users
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Simulate user 1 operations
        Runnable user1Task = () -> {
            try {
                User user = userService.login("user1@example.com", "Password123");
                userService.updatePreferences(user, "Technology");
                userService.addToReadingHistory(user, "article-1");
                System.out.println("User 1 updated preferences and reading history.");
            } catch (AuthenticationException e) {
                System.out.println("User 1 failed to log in: " + e.getMessage());
            }
        };

        // Simulate user 2 operations
        Runnable user2Task = () -> {
            try {
                User user = userService.login("user2@example.com", "Password123");
                userService.updatePreferences(user, "Health");
                userService.addToReadingHistory(user, "article-2");
                System.out.println("User 2 updated preferences and reading history.");
            } catch (AuthenticationException e) {
                System.out.println("User 2 failed to log in: " + e.getMessage());
            }
        };

        // Simulate user 3 operations
        Runnable user3Task = () -> {
            try {
                User user = userService.login("user3@example.com", "Password123");
                userService.updatePreferences(user, "Sports");
                userService.addToReadingHistory(user, "article-3");
                System.out.println("User 3 updated preferences and reading history.");
            } catch (AuthenticationException e) {
                System.out.println("User 3 failed to log in: " + e.getMessage());
            }
        };

        // Submit tasks to the executor
        executor.submit(user1Task);
        executor.submit(user2Task);
        executor.submit(user3Task);

        // Shutdown executor and wait for tasks to complete
        executor.shutdown();
        try {
            if (executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("\nAll tasks completed.\n");

                // Verify results
                verifyResults(userService);
            } else {
                System.out.println("\nTimeout occurred before all tasks could complete.");
            }
        } catch (InterruptedException e) {
            System.out.println("Error while waiting for tasks to complete: " + e.getMessage());
        }
    }

    private static void verifyResults(UserService userService) {
        // Validate user 1's data
        User user1 = userService.getUserDatabase().get("user1@example.com");
        if (user1 != null) {
            System.out.println("User 1 Preferences: " + user1.getPreferences());
            System.out.println("User 1 Reading History: " + user1.getReadingHistory());
        }

        // Validate user 2's data
        User user2 = userService.getUserDatabase().get("user2@example.com");
        if (user2 != null) {
            System.out.println("User 2 Preferences: " + user2.getPreferences());
            System.out.println("User 2 Reading History: " + user2.getReadingHistory());
        }

        // Validate user 3's data
        User user3 = userService.getUserDatabase().get("user3@example.com");
        if (user3 != null) {
            System.out.println("User 3 Preferences: " + user3.getPreferences());
            System.out.println("User 3 Reading History: " + user3.getReadingHistory());
        }
    }
}
