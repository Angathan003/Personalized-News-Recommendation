package org.example.model;

import java.io.Serializable;

public class Article implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String content;
    private String publishedAt;
    private String category;

    // Add the 'score' field
    private transient float score; // Use 'transient' if you don't want to serialize this field

    public Article(String id, String author, String title, String description, String url, String urlToImage, String content, String publishedAt, String category) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.content = content;
        this.publishedAt = publishedAt;
        this.category = category;
        this.score = 0.0f; // Initialize the score
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getContent() {
        return content;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getCategory() {
        return category;
    }

    // Setter for category
    public void setCategory(String category) {
        this.category = category;
    }

    // Getter and Setter for 'score'
    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
