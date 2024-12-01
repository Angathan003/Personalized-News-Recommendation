package org.example.personalizednewsrecommendation.services;

import org.example.personalizednewsrecommendation.models.Article;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleManager {
    private final List<Article> articles = new ArrayList<>();

    // Constructor to initialize from a file
    public ArticleManager(String filePath) {
        loadArticlesFromFile(filePath);
    }

    // Default constructor for manual article management
    public ArticleManager() {
        // No initialization logic
    }

    // Method to add an article
    public void addArticle(String index, String author, String datePublished, String category, String section, String url, String headline, String description, String keywords, String secondHeadline, String articleText) {
        Article article = new Article(index, author, datePublished, category, section, url, headline, description, keywords, secondHeadline, articleText);
        articles.add(article);
    }

    // Method to retrieve all articles
    public List<Article> getArticles() {
        return new ArrayList<>(articles);
    }

    // Method to retrieve article titles only
    public List<String> getArticleTitles() {
        return articles.stream()
                .map(Article::getHeadline) // Use the "headline" as the title
                .collect(Collectors.toList());
    }

    // **Newly Added Method**: Retrieve article by title
    public Article getArticleByTitle(String headline) {
        return articles.stream()
                .filter(article -> article.getHeadline().equalsIgnoreCase(headline))
                .findFirst()
                .orElse(null); // Return null if no matching article is found
    }

    // Method to delete an article by title
    public boolean deleteArticle(String headline) {
        return articles.removeIf(article -> article.getHeadline().equalsIgnoreCase(headline));
    }

    // Method to update an article by title
    public boolean updateArticle(String headline, String newHeadline, String newContent) {
        for (Article article : articles) {
            if (article.getHeadline().equalsIgnoreCase(headline)) {
                article.setHeadline(newHeadline);
                article.setArticleText(newContent);
                return true; // Update successful
            }
        }
        return false; // No matching article found
    }

    // Private helper to load articles from a file
    private void loadArticlesFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // Split CSV with empty values included
                if (parts.length == 11) {
                    addArticle(
                            parts[0].trim(), // Index
                            parts[1].trim(), // Author
                            parts[2].trim(), // Date published
                            parts[3].trim(), // Category
                            parts[4].trim(), // Section
                            parts[5].trim(), // URL
                            parts[6].trim(), // Headline
                            parts[7].trim(), // Description
                            parts[8].trim(), // Keywords
                            parts[9].trim(), // Second headline
                            parts[10].trim() // Article text
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading articles from file: " + e.getMessage());
        }
    }
}
