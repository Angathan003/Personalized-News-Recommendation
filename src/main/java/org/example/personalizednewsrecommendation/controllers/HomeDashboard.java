package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeDashboard {

    private final String username;

    public HomeDashboard(String username) {
        this.username = username;
    }

    public Scene getHomeScene(Stage stage) {
        Button viewArticlesButton = new Button("View Articles");
        Button settingsButton = new Button("Settings");
        Button logoutButton = new Button("Logout");

        viewArticlesButton.setOnAction(e -> System.out.println("Navigating to articles for user: " + username));
        settingsButton.setOnAction(e -> System.out.println("Navigating to settings for user: " + username));
        logoutButton.setOnAction(e -> stage.setScene(new LoginScreen().getLoginScene(stage)));

        VBox layout = new VBox(15, viewArticlesButton, settingsButton, logoutButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        return new Scene(layout, 400, 300);
    }
}
