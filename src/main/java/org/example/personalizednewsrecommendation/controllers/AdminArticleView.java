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

public class AdminArticleView {

    private final ArticleManager articleManager;

    public AdminArticleView(ArticleManager articleManager) {
        this.articleManager = articleManager;
    }

    public Scene getArticleViewScene(Stage stage) {
        // Get all articles
        List<Article> articles = articleManager.getAllArticles();

        // Create a ListView to display articles
        ListView<String> articleList = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        for (Article article : articles) {
            items.add(article.getId() + ". " + article.getTitle() + " (" + article.getCategory() + ")");
        }
        articleList.setItems(items);

        // Back button to return to AdminDashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e ->
                stage.setScene(new AdminDashboard(articleManager).getAdminScene(stage))
        );

        VBox layout = new VBox(10, articleList, backButton);
        return new Scene(layout, 600, 400);
    }
}
