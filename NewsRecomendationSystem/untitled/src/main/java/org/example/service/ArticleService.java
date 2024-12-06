package org.example.service;

import org.example.exceptions.APIException;
import org.example.model.Article;
import org.example.util.FileHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ArticleService {
    private static final String API_KEY = "c3407fa1ff8442fcb4afe39db09f7e5c";
    private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_KEY;
    private List<Article> articles;
    protected FileHandler fileHandler;

    public ArticleService() {
        this.articles = new ArrayList<>();
        this.fileHandler = new FileHandler();
        loadArticles();
    }

    private void loadArticles() {
        try {
            List<Article> articlesFromFile = fileHandler.readArticlesFromFile();
            if (articlesFromFile != null) {
                articles.addAll(articlesFromFile);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load articles from file: " + e.getMessage());
        }
    }

    /**
     * Fetches articles from the News API and categorizes them.
     *
     * @throws APIException If fetching articles fails.
     */
    public void fetchArticles() throws APIException {
        try {
            URL url = new URL(NEWS_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Check if the connection is made
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new APIException("Failed to fetch articles. HttpResponseCode: " + responseCode, null);
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

                    // Determine the category before creating the Article object
                    String category = categorizeContent(title, description, content);

                    // Create the Article object with all required parameters
                    Article article = new Article(id, author, title, description, urlStr, urlToImage, content, publishedAt, category);
                    articles.add(article);
                }

                // Save articles to file
                fileHandler.writeArticlesToFile(articles);
            }
        } catch (IOException e) {
            throw new APIException("An error occurred while fetching articles from the API.", e);
        }
    }

    /**
     * Categorizes content based on keywords.
     *
     * @param title       The title of the article.
     * @param description The description of the article.
     * @param content     The content of the article.
     * @return The determined category.
     */
    public String categorizeContent(String title, String description, String content) {
        String combinedContent = (title + " " + description + " " + content).toLowerCase();

        if (combinedContent.contains("technology") || combinedContent.contains("tech") || combinedContent.contains("software") || combinedContent.contains("hardware")) {
            return "Technology";
        } else if (combinedContent.contains("health") || combinedContent.contains("medicine") || combinedContent.contains("medical")) {
            return "Health";
        } else if (combinedContent.contains("sports") || combinedContent.contains("game") || combinedContent.contains("football") || combinedContent.contains("basketball")) {
            return "Sports";
        } else if (combinedContent.contains("ai") || combinedContent.contains("artificial intelligence") || combinedContent.contains("machine learning")) {
            return "AI";
        } else {
            return "General";
        }
    }

    public List<Article> getArticles() {
        if (articles.isEmpty()) {
            try {
                articles = fileHandler.readArticlesFromFile();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Failed to load articles from file: " + e.getMessage());
            }
        }
        return articles;
    }

    public Article getArticleById(String articleId) {
        for (Article article : articles) {
            if (article.getId().equals(articleId)) {
                return article;
            }
        }
        return null; // Article not found
    }

    /**
     * Adds a new news source by its API URL.
     *
     * @param apiUrl The API URL of the new news source.
     * @throws APIException If adding the source or fetching articles fails.
     */
    public void addSource(String apiUrl) throws APIException {
        // Implement logic to add and fetch articles from the new API source
        try {
            // Example: Modify NEWS_API_URL or maintain a list of sources
            // For demonstration, we'll fetch articles from the new source URL
            String newSourceUrl = apiUrl + "&apiKey=" + API_KEY;
            URL url = new URL(newSourceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new APIException("Failed to fetch articles from the new source. HttpResponseCode: " + responseCode, null);
            } else {
                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                JsonObject jsonObject = JsonParser.parseString(inline.toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray("articles");

                Gson gson = new Gson();

                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject articleJson = jsonArray.get(i).getAsJsonObject();

                    String id = "new-source-" + i;
                    String title = articleJson.get("title").isJsonNull() ? "" : articleJson.get("title").getAsString();
                    String author = articleJson.get("author").isJsonNull() ? "" : articleJson.get("author").getAsString();
                    String description = articleJson.get("description").isJsonNull() ? "" : articleJson.get("description").getAsString();
                    String urlStr = articleJson.get("url").isJsonNull() ? "" : articleJson.get("url").getAsString();
                    String urlToImage = articleJson.get("urlToImage").isJsonNull() ? "" : articleJson.get("urlToImage").getAsString();
                    String publishedAt = articleJson.get("publishedAt").isJsonNull() ? "" : articleJson.get("publishedAt").getAsString();
                    String content = articleJson.get("content").isJsonNull() ? "" : articleJson.get("content").getAsString();

                    String category = categorizeContent(title, description, content);

                    Article article = new Article(id, author, title, description, urlStr, urlToImage, content, publishedAt, category);
                    articles.add(article);
                }

                // Save updated articles list
                fileHandler.writeArticlesToFile(articles);
                System.out.println("New source added and articles fetched successfully.");
            }
        } catch (IOException e) {
            throw new APIException("An error occurred while adding the new news source.", e);
        }
    }

    public void addArticle(Article article) {
        articles.add(article);
        try {
            fileHandler.writeArticlesToFile(articles);
            System.out.println("Article added successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save articles: " + e.getMessage());
        }
    }
}
