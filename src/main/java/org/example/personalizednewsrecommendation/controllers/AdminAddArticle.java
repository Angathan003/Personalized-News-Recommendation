package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class AdminAddArticle {

    private final ArticleManager articleManager;

    public AdminAddArticle(ArticleManager articleManager) {
        this.articleManager = articleManager;
    }

    public Scene getAddArticleScene(Stage stage) {
        // Input fields for article details
        TextField titleField = new TextField();
        titleField.setPromptText("Enter article title: ");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Enter article category: ");

        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Enter article content");

        // Buttons for actions
        Button addButton = new Button("Add Article");
        Button backButton = new Button("Back");

        // Add button action
        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String category = categoryField.getText();
            String content = contentArea.getText();

            if (title.isEmpty() || category.isEmpty() || content.isEmpty()) {
                Alerts.showError("All fields are required!");
            } else {
                articleManager.addArticle(title, category, content);
                Alerts.showSuccess("Article added successfully!");
                titleField.clear();
                categoryField.clear();
                contentArea.clear();
            }
        });

        // Back button action
        backButton.setOnAction(e ->
                stage.setScene(new AdminDashboard(articleManager).getAdminScene(stage))
        );

        // Layout
        VBox layout = new VBox(10, titleField, categoryField, contentArea, addButton, backButton);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 400, 400);
    }
}
