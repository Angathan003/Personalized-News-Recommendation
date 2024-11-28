package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class AdminDeleteArticle {

    private final ArticleManager articleManager;

    public AdminDeleteArticle(ArticleManager articleManager) {
        this.articleManager = articleManager;
    }

    public Scene getDeleteArticleScene(Stage stage) {
        // Input field for article title
        TextField titleField = new TextField();
        titleField.setPromptText("Enter article title to delete");

        // Buttons for actions
        Button deleteButton = new Button("Delete Article");
        Button backButton = new Button("Back");

        // Delete button action
        deleteButton.setOnAction(e -> {
            String title = titleField.getText();

            if (title.isEmpty()) {
                Alerts.showError("Please enter the title of the article to delete!");
            } else {
                articleManager.deleteArticle(title);
                Alerts.showSuccess("Article deleted successfully!");
                titleField.clear();
            }
        });

        // Back button action
        backButton.setOnAction(e ->
                stage.setScene(new AdminDashboard(articleManager).getAdminScene(stage))
        );

        // Layout
        VBox layout = new VBox(10, titleField, deleteButton, backButton);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 400, 300);
    }
}
