package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.models.Article;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.services.UserManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class AdminDeleteArticle {
    private final UserManager userManager;
    private final ArticleManager articleManager;

    public AdminDeleteArticle(UserManager userManager, ArticleManager articleManager) {
        this.userManager = userManager;
        this.articleManager = articleManager;
    }

    public Scene getDeleteArticleScene(Stage stage) {
        Label titleLabel = new Label("Select an Article to Delete:");
        ListView<String> articleListView = new ListView<>();
        articleListView.getItems().addAll(articleManager.getArticleTitles());

        Button deleteButton = new Button("Delete");
        Button backButton = new Button("Back");

        deleteButton.setOnAction(e -> {
            String selectedTitle = articleListView.getSelectionModel().getSelectedItem();
            if (selectedTitle != null) {
                articleManager.deleteArticle(selectedTitle);
                Alerts.showSuccess("Article deleted successfully!");
                stage.setScene(new AdminDashboard(userManager, articleManager).getDashboardScene(stage));
            } else {
                Alerts.showError("Please select an article to delete.");
            }
        });

        backButton.setOnAction(e -> stage.setScene(new AdminDashboard(userManager, articleManager).getDashboardScene(stage)));

        VBox layout = new VBox(10, titleLabel, articleListView, deleteButton, backButton);
        layout.setAlignment(Pos.CENTER);
        return new Scene(layout, 400, 400);
    }
}
