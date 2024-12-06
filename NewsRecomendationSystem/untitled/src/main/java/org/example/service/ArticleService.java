package org.example.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.model.Article;
import org.example.util.FileHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArticleService {
    private static final String API_KEY = "c3407fa1ff8442fcb4afe39db09f7e5c";
    private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_KEY;
    private List<Article> articles;
    private FileHandler fileHandler;

    public ArticleService() {
        this.articles = new ArrayList<>();
        this.fileHandler = new FileHandler();
    }

    public void fetchArticles() {
        try {
            URL url = new URL(NEWS_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Check if the connection is made
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                // Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                // Close the scanner
                scanner.close();

                // Parse JSON data
                JsonObject jsonObject = JsonParser.parseString(inline.toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray("articles");

                Gson gson = new Gson();

                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject articleJson = jsonArray.get(i).getAsJsonObject();

                    String id = "article-" + i;
                    String title = articleJson.get("title").isJsonNull() ? "" : articleJson.get("title").getAsString();
                    String author = articleJson.get("author").isJsonNull() ? "" : articleJson.get("author").getAsString();
                    String description = articleJson.get("description").isJsonNull() ? "" : articleJson.get("description").getAsString();
                    String urlStr = articleJson.get("url").isJsonNull() ? "" : articleJson.get("url").getAsString();
                    String urlToImage = articleJson.get("urlToImage").isJsonNull() ? "" : articleJson.get("urlToImage").getAsString();
                    String publishedAt = articleJson.get("publishedAt").isJsonNull() ? "" : articleJson.get("publishedAt").getAsString();
                    String content = articleJson.get("content").isJsonNull() ? "" : articleJson.get("content").getAsString();

                    Article article = new Article(id, title, author, description, urlStr, urlToImage, publishedAt, content);
                    articles.add(article);
                }

                // Save articles to file
                fileHandler.writeArticlesToFile(articles);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Article> getArticles() {
        if (articles.isEmpty()) {
            articles = fileHandler.readArticlesFromFile();
        }
        return articles;
    }

    public void categorizeArticles() {
        for (Article article : articles) {
            categorizeArticle(article);
        }
        // Save categorized articles
        fileHandler.writeArticlesToFile(articles);
    }

    public void categorizeArticle(Article article) {
        String content = article.getTitle() + " " + article.getDescription() + " " + article.getContent();
        content = content.toLowerCase();

        if (content.contains("technology") || content.contains("tech") || content.contains("software") || content.contains("hardware")) {
            article.setCategory("Technology");
        } else if (content.contains("health") || content.contains("medicine") || content.contains("medical")) {
            article.setCategory("Health");
        } else if (content.contains("sports") || content.contains("game") || content.contains("football") || content.contains("basketball")) {
            article.setCategory("Sports");
        } else if (content.contains("ai") || content.contains("artificial intelligence") || content.contains("machine learning")) {
            article.setCategory("AI");
        } else {
            article.setCategory("General");
        }
    }

    public Article getArticleById(String articleId) {
        for (Article article : articles) {
            if (article.getId().equals(articleId)) {
                return article;
            }
        }
        return null; // Article not found
    }
}
