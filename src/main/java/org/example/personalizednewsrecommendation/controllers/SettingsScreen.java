package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class SettingsScreen {

    private final String username; // Store the username

    // Constructor to initialize username
    public SettingsScreen(String username) {
        this.username = username;
    }

    public Scene getSettingsScene(Stage stage) {
        Label preferencesLabel = new Label("Preferences:");
        TextField preferencesField = new TextField();
        Button saveButton = new Button("Save");
        Button backButton = new Button("Back");

        // Save preferences
        saveButton.setOnAction(e -> {
            // Save logic here (currently just a placeholder)
            Alerts.showSuccess("Preferences saved!");
        });

        // Navigate back to HomeDashboard with the username
        backButton.setOnAction(e -> stage.setScene(new HomeDashboard(username).getHomeScene(stage)));

        VBox layout = new VBox(10, preferencesLabel, preferencesField, saveButton, backButton);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 400, 300);
    }
}
