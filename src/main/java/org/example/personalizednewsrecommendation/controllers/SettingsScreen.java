package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.services.UserManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class SettingsScreen {
    private final UserManager userManager;
    private final ArticleManager articleManager;
    private final String username;

    public SettingsScreen(UserManager userManager, ArticleManager articleManager, String username) {
        this.userManager = userManager;
        this.articleManager = articleManager;
        this.username = username;
    }

    public Scene getSettingsScene(Stage stage) {
        Label preferencesLabel = new Label("Set Preferences (e.g., Technology, Sports):");
        TextField preferencesField = new TextField();

        Button saveButton = new Button("Save");
        Button backButton = new Button("Back");

        // Save button logic
        saveButton.setOnAction(e -> {
            String preferences = preferencesField.getText();
            if (!preferences.isEmpty()) {
                userManager.updateUserPreferences(username, preferences);
                Alerts.showSuccess("Preferences saved successfully!");
            } else {
                Alerts.showError("Preferences cannot be empty!");
            }
        });

        // Back button logic
        backButton.setOnAction(e -> stage.setScene(new HomeDashboard(userManager, articleManager, username).getHomeScene(stage)));

        VBox layout = new VBox(10, preferencesLabel, preferencesField, saveButton, backButton);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 400, 300);
    }
}
