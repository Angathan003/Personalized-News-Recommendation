package org.example.personalizednewsrecommendation.services;

import org.example.personalizednewsrecommendation.models.Article;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendationEngine {

    public List<Article> recommendArticles(String preference, List<Article> allArticles) {
        return allArticles.stream()
                .filter(article -> article.getCategory().equalsIgnoreCase(preference))
                .collect(Collectors.toList());
    }

    public List<Article> recommendByKeywords(String keyword, List<Article> allArticles) {
        return allArticles.stream()
                .filter(article -> article.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        article.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
