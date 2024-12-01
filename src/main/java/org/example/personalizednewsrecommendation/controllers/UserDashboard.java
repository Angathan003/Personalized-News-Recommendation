package org.example.personalizednewsrecommendation.controllers;

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

public class UserDashboard {
    private final UserManager userManager;
    private final ArticleManager articleManager;
    private final String username;

    public UserDashboard(UserManager userManager, ArticleManager articleManager, String username) {
        this.userManager = userManager;
        this.articleManager = articleManager;
        this.username = username;
    }

    public Scene getDashboardScene(Stage stage) {
        if (articleManager == null) {
            Alerts.showError("Article Manager is not initialized!");
            return new Scene(new VBox(new Label("Error: Article Manager is null")), 400, 400);
        }

        ListView<String> articleListView = new ListView<>();
        articleListView.getItems().addAll(articleManager.getArticleTitles());

        Button viewArticleButton = new Button("View Article");
        Button settingsButton = new Button("Settings");
        Button logoutButton = new Button("Logout");

        viewArticleButton.setOnAction(e -> {
            String selectedTitle = articleListView.getSelectionModel().getSelectedItem();
            if (selectedTitle != null) {
                Article selectedArticle = articleManager.getArticleByTitle(selectedTitle);
                if (selectedArticle != null) {
                    Alerts.showSuccess("Article Content: " + selectedArticle.getArticleText());
                } else {
                    Alerts.showError("Article not found!");
                }
            } else {
                Alerts.showError("Please select an article!");
            }
        });

        settingsButton.setOnAction(e -> stage.setScene(new SettingsScreen(userManager, articleManager, username).getSettingsScene(stage)));
        logoutButton.setOnAction(e -> stage.setScene(new LoginScreen(userManager, articleManager).getLoginScene(stage)));

        VBox layout = new VBox(10, articleListView, viewArticleButton, settingsButton, logoutButton);
        return new Scene(layout, 400, 400);
    }

}
