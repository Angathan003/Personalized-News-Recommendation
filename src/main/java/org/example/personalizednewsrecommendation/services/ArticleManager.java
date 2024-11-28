package org.example.personalizednewsrecommendation.services;

import org.example.personalizednewsrecommendation.models.Article;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleManager {
    private final List<Article> articles = new ArrayList<>();
    private final String datasetPath;

    public ArticleManager(String datasetPath) {
        this.datasetPath = datasetPath;
        loadArticlesFromCSV();
    }

    // Load articles from the CSV file
    private void loadArticlesFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(datasetPath))) {
            String line;
            // Skip the header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) { // Ensure there are enough columns
                    articles.add(new Article(data[0], data[1], data[2], data[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading dataset: " + e.getMessage());
        }
    }

    // Save articles back to the CSV file
    private void saveArticlesToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(datasetPath))) {
            // Write header
            bw.write("id,title,category,content");
            bw.newLine();
            for (Article article : articles) {
                bw.write(String.join(",", article.getId(), article.getTitle(), article.getCategory(), article.getContent()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving dataset: " + e.getMessage());
        }
    }

    public List<Article> getAllArticles() {
        return articles;
    }

    public void addArticle(String title, String category, String content) {
        String id = String.valueOf(articles.size() + 1);
        articles.add(new Article(id, title, category, content));
        saveArticlesToCSV();
    }

    public void updateArticle(String oldTitle, String newTitle, String category, String content) {
        for (Article article : articles) {
            if (article.getTitle().equalsIgnoreCase(oldTitle)) {
                article.setTitle(newTitle);
                article.setCategory(category);
                article.setContent(content);
                saveArticlesToCSV();
                return;
            }
        }
        System.out.println("Article not found: " + oldTitle);
    }

    public void deleteArticle(String title) {
        articles.removeIf(article -> article.getTitle().equalsIgnoreCase(title));
        saveArticlesToCSV();
    }
}
