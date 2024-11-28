package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class SettingsScreen {

    private final String username; // Store the username
    private final ArticleManager articleManager; // Store the ArticleManager

    // Constructor to initialize username and ArticleManager
    public SettingsScreen(String username, ArticleManager articleManager) {
        this.username = username;
        this.articleManager = articleManager;
    }

    public Scene getSettingsScene(Stage stage) {
        Label preferencesLabel = new Label("Preferences:");
        TextField preferencesField = new TextField();
        preferencesField.setPromptText("Enter your preferences (e.g., Technology, Health)");

        Button saveButton = new Button("Save");
        Button backButton = new Button("Back");

        // Save preferences
        saveButton.setOnAction(e -> {
            String preferences = preferencesField.getText();
            if (preferences.isEmpty()) {
                Alerts.showError("Please enter valid preferences!");
            } else {
                // Save logic here (currently just a placeholder)
                // In a real app, you would persist preferences to a database or file.
                Alerts.showSuccess("Preferences saved for " + username + ": " + preferences);
            }
        });

        // Navigate back to HomeDashboard with the username and ArticleManager
        backButton.setOnAction(e ->
                stage.setScene(new HomeDashboard(username, articleManager).getHomeScene(stage))
        );

        VBox layout = new VBox(10, preferencesLabel, preferencesField, saveButton, backButton);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 400, 300);
    }
}
