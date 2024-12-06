package org.example.service;

import org.example.model.Article;
import org.example.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RecommendationService {
    private ArticleService articleService;

    public RecommendationService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public List<Article> getRecommendations(User user) {
        List<Article> allArticles = articleService.getArticles();
        List<Article> recommendedArticles = new ArrayList<>();
        Map<String, Integer> preferences = user.getPreferences();

        for (Article article : allArticles) {
            // Skip articles the user has already read
            if (user.getReadingHistory().contains(article.getId())) {
                continue;
            }

            // Assign score based on user preferences
            int score = 0;
            if (preferences.containsKey(article.getCategory())) {
                int preferenceCount = preferences.get(article.getCategory());
                score += preferenceCount * 10; // Adjust the multiplier as needed
            }

            // You can add more scoring logic here (e.g., based on keywords, authors)

            if (score > 0) {
                article.setScore(score);
                recommendedArticles.add(article);
            }
        }

        // Sort articles by score in descending order
        Collections.sort(recommendedArticles, new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                return o2.getScore() - o1.getScore();
            }
        });

        return recommendedArticles;
    }
}
