package org.example;

import org.example.exceptions.*;
import org.example.model.Article;
import org.example.model.Admin;
import org.example.model.User;
import org.example.service.ArticleService;
import org.example.service.MLRecommendationService;
import org.example.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static UserService userService = new UserService();
    private static ArticleService articleService = new ArticleService();
    private static User currentUser;
    private static MLRecommendationService mlRecommendationService;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Initial choice: User or Admin
            System.out.println("Welcome to the News Recommendation System");
            System.out.print("Are you a User or Admin? (Enter 'user' or 'admin'): ");
            String userType = scanner.nextLine().trim().toLowerCase();

            if (userType.equals("user")) {
                handleUserAuthentication(scanner);
            } else if (userType.equals("admin")) {
                handleAdminAuthentication(scanner);
            } else {
                System.out.println("Invalid choice. Exiting the application.");
                System.exit(0);
            }

            // Fetch articles (categorization is handled within this method)
            try {
                articleService.fetchArticles();
                System.out.println("Articles fetched successfully.");
            } catch (APIException e) {
                System.out.println("Failed to fetch articles: " + e.getMessage());
                // Depending on requirements, you might choose to exit or continue with existing articles
            }

            // Initialize MLRecommendationService
            try {
                mlRecommendationService = new MLRecommendationService(articleService.getArticles());
            } catch (Exception e) {
                System.out.println("Failed to initialize recommendation engine: " + e.getMessage());
                System.exit(0);
            }

            // Main loop
            while (true) {
                // Display articles to user
                List<Article> articles = articleService.getArticles();
                System.out.println("\nAvailable Articles:");
                for (int i = 0; i < articles.size(); i++) {
                    Article article = articles.get(i);
                    System.out.println(i + 1 + ". [" + article.getCategory() + "] " + article.getTitle());
                }

                System.out.println("\nOptions:");
                System.out.println("1. View recommended articles");
                System.out.println("2. Read an article");
                System.out.println("3. View reading history");
                System.out.println("4. Exit");
                System.out.println("5. Manage Profile");
                if (currentUser instanceof Admin) {
                    System.out.println("6. Administer Articles");
                }
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        viewRecommendedArticles(scanner);
                        break;
                    case "2":
                        readArticle(scanner, articles);
                        break;
                    case "3":
                        viewReadingHistory();
                        break;
                    case "4":
                        System.out.println("Thank you for using the News Recommendation System!");
                        System.exit(0);
                        break;
                    case "5":
                        manageProfile(scanner);
                        break;
                    case "6":
                        if (currentUser instanceof Admin) {
                            administerArticles(scanner);
                        } else {
                            System.out.println("Invalid choice.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                }
            }
        } catch (Exception e) {
            // Catch-all for any unexpected exceptions
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void handleUserAuthentication(Scanner scanner) {
        while (true) { // Loop until a valid input is received
            System.out.print("Do you have an account? (yes/no): ");
            String hasAccount = scanner.nextLine().trim().toLowerCase();

            if (hasAccount.equals("yes")) {
                // User Login
                System.out.print("Enter username: ");
                String username = scanner.nextLine().trim();
                System.out.print("Enter password: ");
                String password = scanner.nextLine().trim();
                try {
                    currentUser = userService.login(username, password);
                    if (!(currentUser instanceof Admin) && !currentUser.getRole().equals("user")) {
                        System.out.println("Access denied. Not a regular user.");
                        System.exit(0);
                    }
                    break; // Exit the loop after successful login
                } catch (AuthenticationException e) {
                    System.out.println(e.getMessage());
                    // Optionally, you can allow the user to retry login
                    System.out.print("Would you like to try logging in again? (yes/no): ");
                    String retry = scanner.nextLine().trim().toLowerCase();
                    if (!retry.equals("yes")) {
                        System.out.println("Exiting the application.");
                        System.exit(0);
                    }
                }
            } else if (hasAccount.equals("no")) {
                // User Registration
                System.out.print("Choose a username (must be a valid email): ");
                String username = scanner.nextLine().trim();
                System.out.print("Choose a password: ");
                String password = scanner.nextLine().trim();
                try {
                    userService.createAccount(username, password, "user");
                    currentUser = userService.login(username, password);
                    break; // Exit the loop after successful registration and login
                } catch (InvalidDataException | UserAlreadyExistsException | UnauthorizedActionException e) {
                    System.out.println("Account creation failed: " + e.getMessage());
                    // Optionally, you can allow the user to retry registration
                    System.out.print("Would you like to try creating an account again? (yes/no): ");
                    String retry = scanner.nextLine().trim().toLowerCase();
                    if (!retry.equals("yes")) {
                        System.out.println("Exiting the application.");
                        System.exit(0);
                    }
                } catch (AuthenticationException e) {
                    System.out.println("Authentication failed after account creation: " + e.getMessage());
                    // Optionally, allow retry
                    System.out.print("Would you like to try logging in again? (yes/no): ");
                    String retry = scanner.nextLine().trim().toLowerCase();
                    if (!retry.equals("yes")) {
                        System.out.println("Exiting the application.");
                        System.exit(0);
                    }
                }
            } else {
                // Invalid Input Handling
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                // The loop will continue, prompting the user again
            }
        }
    }

    private static void handleAdminAuthentication(Scanner scanner) {
        // Admins can only login
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        try {
            currentUser = userService.login(username, password);
            if (!(currentUser instanceof Admin)) {
                System.out.println("Access denied. Not an administrator.");
                System.exit(0);
            }
            System.out.println("Admin login successful.");
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private static void manageProfile(Scanner scanner) {
        System.out.println("\nManage Profile:");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("3. View Preferences");
        System.out.println("4. Back");
        System.out.print("Enter your choice (1-4): ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Enter new username (must be a valid email): ");
                String newUsername = scanner.nextLine();
                try {
                    if (userService.updateUsername(currentUser, newUsername)) {
                        System.out.println("Username updated successfully.");
                    }
                } catch (InvalidDataException | UserAlreadyExistsException e) {
                    System.out.println("Failed to update username: " + e.getMessage());
                }
                break;
            case "2":
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();
                try {
                    userService.updatePassword(currentUser, newPassword);
                    System.out.println("Password updated successfully.");
                } catch (InvalidDataException e) {
                    System.out.println("Failed to update password: " + e.getMessage());
                }
                break;
            case "3":
                System.out.println("Your Preferences:");
                for (Map.Entry<String, Integer> entry : currentUser.getPreferences().entrySet()) {
                    System.out.println("- " + entry.getKey() + ": " + entry.getValue());
                }
                break;
            case "4":
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void administerArticles(Scanner scanner) {
        System.out.println("\nAdminister Articles:");
        System.out.println("1. Add Article Source");
        System.out.println("2. Manually Add Article");
        System.out.println("3. Back");
        System.out.print("Enter your choice (1-3): ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Enter the API URL for the new source: ");
                String apiUrl = scanner.nextLine();
                try {
                    articleService.addSource(apiUrl);
                } catch (APIException e) {
                    System.out.println("Failed to add new source: " + e.getMessage());
                }
                break;
            case "2":
                System.out.print("Enter article title: ");
                String title = scanner.nextLine();
                System.out.print("Enter article description: ");
                String description = scanner.nextLine();
                System.out.print("Enter article content: ");
                String content = scanner.nextLine();
                System.out.print("Enter article author: ");
                String author = scanner.nextLine();
                System.out.print("Enter article published date (e.g., 2023-10-04): ");
                String publishedAt = scanner.nextLine();
                System.out.print("Enter article URL: ");
                String url = scanner.nextLine();
                System.out.print("Enter article image URL: ");
                String urlToImage = scanner.nextLine();

                // Generate a unique ID for the article
                String id = "manual-" + System.currentTimeMillis();

                // Determine the category of the article
                String category = articleService.categorizeContent(title, description, content);

                // Create new Article object
                Article newArticle = new Article(id, author, title, description, url, urlToImage, content, publishedAt, category);

                articleService.addArticle(newArticle);
                break;

            case "3":
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void viewRecommendedArticles(Scanner scanner) {
        try {
            List<Article> recommendations = mlRecommendationService.getRecommendations(currentUser, 5);
            if (recommendations.isEmpty()) {
                System.out.println("\nNo recommendations available. Please read some articles to build your profile.");
                return;
            }

            System.out.println("\nRecommended Articles:");
            for (int i = 0; i < recommendations.size(); i++) {
                Article article = recommendations.get(i);
                System.out.println(i + 1 + ". [" + article.getCategory() + "] " + article.getTitle());
            }

            System.out.print("Enter the number of the article you want to read (or 0 to go back): ");
            String input = scanner.nextLine();
            try {
                int articleNumber = Integer.parseInt(input);
                if (articleNumber == 0) {
                    return;
                }
                if (articleNumber < 1 || articleNumber > recommendations.size()) {
                    System.out.println("Invalid article number.");
                    return;
                }
                Article selectedArticle = recommendations.get(articleNumber - 1);

                // Display article details
                System.out.println("\nReading Article:");
                System.out.println("Title: " + selectedArticle.getTitle());
                System.out.println("Author: " + selectedArticle.getAuthor());
                System.out.println("Published At: " + selectedArticle.getPublishedAt());
                System.out.println("Content: " + selectedArticle.getContent());
                System.out.println("URL: " + selectedArticle.getUrl());

                // Update reading history and preferences
                userService.addToReadingHistory(currentUser, selectedArticle.getId());
                userService.updatePreferences(currentUser, selectedArticle.getCategory());

                System.out.println("\nArticle has been added to your reading history.");
                System.out.println("Your preferences have been updated based on this article's category.");

                // Prompt user to rate the article
                rateArticle(scanner, selectedArticle);

                // Re-initialize the MLRecommendationService to include the new user data
                try {
                    mlRecommendationService = new MLRecommendationService(articleService.getArticles());
                } catch (Exception e) {
                    System.out.println("Failed to update recommendation engine: " + e.getMessage());
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Failed to get recommendations: " + e.getMessage());
        }
    }

    private static void readArticle(Scanner scanner, List<Article> articles) {
        System.out.print("Enter the number of the article you want to read: ");
        String input = scanner.nextLine();
        try {
            int articleNumber = Integer.parseInt(input);
            if (articleNumber < 1 || articleNumber > articles.size()) {
                System.out.println("Invalid article number.");
                return;
            }
            Article selectedArticle = articles.get(articleNumber - 1);

            // Display article details
            System.out.println("\nReading Article:");
            System.out.println("Title: " + selectedArticle.getTitle());
            System.out.println("Author: " + selectedArticle.getAuthor());
            System.out.println("Published At: " + selectedArticle.getPublishedAt());
            System.out.println("Content: " + selectedArticle.getContent());
            System.out.println("URL: " + selectedArticle.getUrl());

            // Update reading history and preferences
            userService.addToReadingHistory(currentUser, selectedArticle.getId());
            userService.updatePreferences(currentUser, selectedArticle.getCategory());

            System.out.println("\nArticle has been added to your reading history.");
            System.out.println("Your preferences have been updated based on this article's category.");

            // Prompt user to rate the article
            rateArticle(scanner, selectedArticle);

            // Re-initialize the MLRecommendationService to include the new user data
            try {
                mlRecommendationService = new MLRecommendationService(articleService.getArticles());
            } catch (Exception e) {
                System.out.println("Failed to update recommendation engine: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("An error occurred while reading the article: " + e.getMessage());
        }
    }

    private static void rateArticle(Scanner scanner, Article selectedArticle) {
        // Prompt user to rate the article
        System.out.print("Please rate this article (1-5): ");
        String ratingInput = scanner.nextLine();
        try {
            int rating = Integer.parseInt(ratingInput);
            if (rating >= 1 && rating <= 5) {
                userService.rateArticle(currentUser, selectedArticle.getId(), rating);
                System.out.println("Thank you for rating!");
            } else {
                System.out.println("Invalid rating. Please enter a number between 1 and 5.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Rating not recorded.");
        }
    }

    private static void viewReadingHistory() {
        System.out.println("\nYour Reading History:");
        List<String> readingHistory = currentUser.getReadingHistory();
        if (readingHistory.isEmpty()) {
            System.out.println("You haven't read any articles yet.");
        } else {
            for (String articleId : readingHistory) {
                // Find the article by ID
                Article article = articleService.getArticleById(articleId);
                if (article != null) {
                    System.out.println("- [" + article.getCategory() + "] " + article.getTitle());
                }
            }
        }
    }
}
