package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class AdminUpdateArticle {

    private final ArticleManager articleManager;

    public AdminUpdateArticle(ArticleManager articleManager) {
        this.articleManager = articleManager;
    }

    public Scene getUpdateArticleScene(Stage stage) {
        // Input fields for updating article details
        TextField oldTitleField = new TextField();
        oldTitleField.setPromptText("Enter existing article title");

        TextField newTitleField = new TextField();
        newTitleField.setPromptText("Enter new title");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Enter new category");

        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Enter new content");

        // Buttons for actions
        Button updateButton = new Button("Update Article");
        Button backButton = new Button("Back");

        // Update button action
        updateButton.setOnAction(e -> {
            String oldTitle = oldTitleField.getText();
            String newTitle = newTitleField.getText();
            String category = categoryField.getText();
            String content = contentArea.getText();

            if (oldTitle.isEmpty() || newTitle.isEmpty() || category.isEmpty() || content.isEmpty()) {
                Alerts.showError("All fields are required!");
            } else {
                articleManager.updateArticle(oldTitle, newTitle, category, content);
                Alerts.showSuccess("Article updated successfully!");
                oldTitleField.clear();
                newTitleField.clear();
                categoryField.clear();
                contentArea.clear();
            }
        });

        // Back button action
        backButton.setOnAction(e ->
                stage.setScene(new AdminDashboard(articleManager).getAdminScene(stage))
        );

        // Layout
        VBox layout = new VBox(10, oldTitleField, newTitleField, categoryField, contentArea, updateButton, backButton);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 400, 400);
    }
}
