package org.example.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.example.model.Article;
import org.example.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MLRecommendationService {
    private List<Article> articles;
    private ByteBuffersDirectory index;
    private Analyzer analyzer;

    public MLRecommendationService(List<Article> articles) throws IOException {
        this.articles = articles;
        this.index = new ByteBuffersDirectory();
        this.analyzer = new EnglishAnalyzer();
        indexArticles();
    }

    private void indexArticles() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);

        for (Article article : articles) {
            Document doc = new Document();
            doc.add(new StringField("id", article.getId(), Field.Store.YES));
            String content = article.getTitle() + " " + article.getContent();
            doc.add(new TextField("content", content, Field.Store.NO));
            doc.add(new StringField("category", article.getCategory().toLowerCase(), Field.Store.NO)); // Ensure category is lowercase
            writer.addDocument(doc);
        }
        writer.close();
    }

    public List<Article> getRecommendations(User user, int topN) throws Exception {
        // Build user profile from reading history
        StringBuilder userProfile = new StringBuilder();
        for (String articleId : user.getReadingHistory()) {
            Article article = getArticleById(articleId);
            if (article != null) {
                userProfile.append(article.getTitle()).append(" ").append(article.getContent()).append(" ");
            }
        }

        System.out.println("User Profile Query: " + userProfile.toString());

        if (userProfile.length() == 0) {
            // If user has no reading history, return popular articles or a default list
            return getDefaultRecommendations(topN);
        }

        // Build boosted query
        QueryParser parser = new QueryParser("content", analyzer);
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        // User profile query
        Query userProfileQuery = parser.parse(QueryParser.escape(userProfile.toString()));
        booleanQuery.add(new BoostQuery(userProfileQuery, 2.0f), BooleanClause.Occur.SHOULD);

        // Boost articles in user's preferred categories
        Map<String, Integer> preferences = user.getPreferences();
        for (String category : preferences.keySet()) {
            TermQuery categoryQuery = new TermQuery(new Term("category", category.toLowerCase()));
            float boost = preferences.get(category);
            booleanQuery.add(new BoostQuery(categoryQuery, boost), BooleanClause.Occur.SHOULD);
        }

        Query finalQuery = booleanQuery.build();
        System.out.println("Constructed Query: " + finalQuery.toString());

        // Search for similar articles
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);

        TopDocs topDocs = searcher.search(finalQuery, topN + user.getReadingHistory().size());

        List<Article> recommendations = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            String articleId = doc.get("id");
            // Exclude articles the user has already read
            if (!user.getReadingHistory().contains(articleId)) {
                Article article = getArticleById(articleId);
                if (article != null) {
                    article.setScore(scoreDoc.score); // Correctly pass float to setScore
                    recommendations.add(article);
                    if (recommendations.size() >= topN) {
                        break;
                    }
                }
            }
        }
        reader.close();
        return recommendations;
    }

    private Article getArticleById(String id) {
        for (Article article : articles) {
            if (article.getId().equals(id)) {
                return article;
            }
        }
        return null;
    }

    private List<Article> getDefaultRecommendations(int topN) {
        // Return top N articles based on some default criteria, e.g., most recent
        List<Article> defaultArticles = new ArrayList<>();
        for (Article article : articles) {
            if (!defaultArticles.contains(article)) {
                defaultArticles.add(article);
                if (defaultArticles.size() >= topN) {
                    break;
                }
            }
        }
        return defaultArticles;
    }
}
