package org.example.personalizednewsrecommendation.controllers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.models.Article;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.services.UserManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class HomeDashboard {
    private final UserManager userManager;
    private final ArticleManager articleManager;
    private final String username;

    public HomeDashboard(UserManager userManager, ArticleManager articleManager, String username) {
        this.userManager = userManager;
        this.articleManager = articleManager;
        this.username = username;
    }

    public Scene getHomeScene(Stage stage) {
        ListView<String> articleListView = new ListView<>();
        articleListView.getItems().addAll(articleManager.getArticleTitles());

//        Button viewContentButton = new Button("View Content");
        Button logoutButton = new Button("Logout");

        Button viewContentButton = new Button("View Articles");

        viewContentButton.setOnAction(e -> {
            ArticleDisplay articleDisplay = new ArticleDisplay(userManager, articleManager, username);
            stage.setScene(articleDisplay.getArticleScene(stage));
        });

//        viewContentButton.setOnAction(e -> {
//            String selectedTitle = articleListView.getSelectionModel().getSelectedItem();
//            if (selectedTitle != null) {
//                Article selectedArticle = articleManager.getArticleByTitle(selectedTitle);
//                if (selectedArticle != null) {
//                    Alerts.showSuccess("Content: \n" + selectedArticle.getArticleText());
//                } else {
//                    Alerts.showError("Article not found.");
//                }
//            } else {
//                Alerts.showError("Please select an article to view.");
//            }
//        });

        logoutButton.setOnAction(e -> stage.setScene(new LoginScreen(userManager, articleManager).getLoginScene(stage)));

        VBox layout = new VBox(10, articleListView, viewContentButton, logoutButton);
        return new Scene(layout, 600, 400);
    }
}
