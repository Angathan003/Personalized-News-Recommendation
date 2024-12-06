package org.example.service;

import org.example.model.Article;
import org.example.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RecommendationService {
    private ArticleService articleService;

    public RecommendationService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public List<Article> getRecommendations(User user, int topN) {
        List<Article> allArticles = articleService.getArticles();
        List<Article> recommendedArticles = new ArrayList<>();
        Map<String, Integer> preferences = user.getPreferences();

        for (Article article : allArticles) {
            // Skip articles the user has already read
            if (user.getReadingHistory().contains(article.getId())) {
                continue;
            }

            // Assign score based on user preferences
            float score = 0.0f;
            if (preferences.containsKey(article.getCategory())) {
                int preferenceCount = preferences.get(article.getCategory());
                score += preferenceCount * 10.0f; // Using float multiplication
            }

            if (user.getRatings().containsKey(article.getId())) {
                int rating = user.getRatings().get(article.getId());
                score += rating * 20.0f; // Using float multiplication
            }

            if (score > 0) {
                article.setScore(score);
                recommendedArticles.add(article);
            }
        }

        // Sort articles by score in descending order using a float comparator
        recommendedArticles.sort(Comparator.comparingDouble(Article::getScore).reversed());

        // If you want to limit the number of recommendations, you can trim the list
        if (recommendedArticles.size() > topN) {
            recommendedArticles = recommendedArticles.subList(0, topN);
        }

        return recommendedArticles;
    }
}
