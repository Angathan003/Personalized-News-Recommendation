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

public class AdminArticleView {
    private final UserManager userManager;
    private final ArticleManager articleManager;

    public AdminArticleView(UserManager userManager, ArticleManager articleManager) {
        this.userManager = userManager;
        this.articleManager = articleManager;
    }

    public Scene getViewScene(Stage stage) {
        ListView<String> articlesListView = new ListView<>();
        articlesListView.getItems().addAll(articleManager.getArticleTitles());

//        Button viewContentButton = new Button("View Content");
        Button backButton = new Button("Back");

//        viewContentButton.setOnAction(e -> {
//            String selectedTitle = articlesListView.getSelectionModel().getSelectedItem();
//            if (selectedTitle != null) {
//                Article selectedArticle = articleManager.getArticleByTitle(selectedTitle);
//                if (selectedArticle != null) {
//                    Alerts.showSuccess("Content: " + selectedArticle.getArticleText());
//                } else {
//                    Alerts.showError("Article not found.");
//                }
//            } else {
//                Alerts.showError("Please select an article to view its content.");
//            }
//        });
        Button viewContentButton = new Button("View Articles");

        viewContentButton.setOnAction(e -> {
            ArticleDisplay articleDisplay = new ArticleDisplay(userManager, articleManager,  "admin");
            stage.setScene(articleDisplay.getArticleScene(stage));
        });


        backButton.setOnAction(e -> stage.setScene(new AdminDashboard(userManager, articleManager).getDashboardScene(stage)));

        VBox layout = new VBox(10, articlesListView, viewContentButton, backButton);
        return new Scene(layout, 400, 300);
    }
}
