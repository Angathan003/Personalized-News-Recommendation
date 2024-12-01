package org.example.personalizednewsrecommendation.services;

import org.example.personalizednewsrecommendation.models.Article;

import java.util.List;
import java.util.stream.Collectors;

public class RecommendationEngine {

    public List<Article> getRecommendations(List<Article> allArticles, String preference) {
        return allArticles.stream()
                .filter(article -> article.getArticleText().toLowerCase().contains(preference.toLowerCase()))
                .collect(Collectors.toList());
    }
}
