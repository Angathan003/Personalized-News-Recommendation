package org.example.personalizednewsrecommendation.controllers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.services.UserManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class AdminUpdateArticle {
    private final UserManager userManager;
    private final ArticleManager articleManager;

    // Constructor accepting both UserManager and ArticleManager
    public AdminUpdateArticle(UserManager userManager, ArticleManager articleManager) {
        this.userManager = userManager;
        this.articleManager = articleManager;
    }

    public Scene getUpdateArticleScene(Stage stage) {
        Label oldTitleLabel = new Label("Old Title:");
        TextField oldTitleField = new TextField();

        Label newTitleLabel = new Label("New Title:");
        TextField newTitleField = new TextField();

        Label newContentLabel = new Label("New Content:");
        TextField newContentField = new TextField();

        Button updateButton = new Button("Update");
        Button backButton = new Button("Back");

        updateButton.setOnAction(e -> {
            String oldTitle = oldTitleField.getText();
            String newTitle = newTitleField.getText();
            String newContent = newContentField.getText();

            if (!oldTitle.isEmpty() && !newTitle.isEmpty() && !newContent.isEmpty()) {
                boolean success = articleManager.updateArticle(oldTitle, newTitle, newContent);
                if (success) {
                    Alerts.showSuccess("Article updated successfully!");
                    stage.setScene(new AdminDashboard(userManager, articleManager).getDashboardScene(stage));
                } else {
                    Alerts.showError("Article not found!");
                }
            } else {
                Alerts.showError("All fields are required!");
            }
        });

        backButton.setOnAction(e -> stage.setScene(new AdminDashboard(userManager, articleManager).getDashboardScene(stage)));

        VBox layout = new VBox(10, oldTitleLabel, oldTitleField, newTitleLabel, newTitleField, newContentLabel, newContentField, updateButton, backButton);
        return new Scene(layout, 400, 400);
    }
}
