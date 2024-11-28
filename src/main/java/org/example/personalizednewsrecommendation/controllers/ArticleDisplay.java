package org.example.personalizednewsrecommendation.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.models.Article;
import org.example.personalizednewsrecommendation.services.ArticleManager;

import java.util.List;

public class ArticleDisplay {

    private final String username;
    private final ArticleManager articleManager;

    public ArticleDisplay(String username, ArticleManager articleManager) {
        this.username = username;
        this.articleManager = articleManager;
    }

    public Scene getArticleScene(Stage stage) {
        // Get recommended articles (modify as needed for recommendation logic)
        List<Article> articles = articleManager.getAllArticles();
        ListView<String> articleList = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        for (Article article : articles) {
            items.add(article.getTitle() + " (" + article.getCategory() + ")");
        }
        articleList.setItems(items);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.setScene(new HomeDashboard(username, articleManager).getHomeScene(stage)));

        VBox layout = new VBox(10, articleList, backButton);
        return new Scene(layout, 600, 400);
    }
}
