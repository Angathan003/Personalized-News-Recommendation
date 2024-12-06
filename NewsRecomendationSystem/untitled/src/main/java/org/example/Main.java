package org.example;

import org.example.model.Article;
import org.example.model.User;
import org.example.service.ArticleService;
import org.example.service.MLRecommendationService;
import org.example.service.UserService;
import org.example.service.RecommendationService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;


// Add RecommendationService instance



public class Main {
    private static UserService userService = new UserService();
    private static ArticleService articleService = new ArticleService();
    private static User currentUser;
    private static MLRecommendationService mlRecommendationService;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // User authentication
        System.out.println("Welcome to the News Recommendation System");
        System.out.print("Do you have an account? (yes/no): ");
        String hasAccount = scanner.nextLine();

        if (hasAccount.equalsIgnoreCase("yes")) {
            // Login
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            currentUser = userService.login(username, password);
        } else {
            // Create account
            System.out.print("Choose a username: ");
            String username = scanner.nextLine();
            System.out.print("Choose a password: ");
            String password = scanner.nextLine();
            boolean accountCreated = userService.createAccount(username, password);
            if (accountCreated) {
                currentUser = userService.login(username, password);
            }
        }

        if (currentUser == null) {
            System.out.println("Unable to proceed without login.");
            System.exit(0);
        }

        // Fetch and categorize articles
        articleService.fetchArticles();
        articleService.categorizeArticles();

        // Initialize MLRecommendationService
        mlRecommendationService = new MLRecommendationService(articleService.getArticles());


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
            System.out.print("Enter your choice (1-4): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewRecommendedArticles(scanner);
                    break;
                case "2":
                    readArticle(scanner, articleService.getArticles());
                    break;
                case "3":
                    viewReadingHistory();
                    break;
                case "4":
                    System.out.println("Thank you for using the News Recommendation System!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1, 2, 3, or 4.");
            }
        }
    }

    private static void viewRecommendedArticles(Scanner scanner) throws Exception {
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

        // Rest of the method remains the same...
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

            // Re-initialize the MLRecommendationService to include the new user data
            mlRecommendationService = new MLRecommendationService(articleService.getArticles());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
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
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
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
