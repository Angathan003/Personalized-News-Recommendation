package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboard {

    public Scene getAdminScene(Stage stage) {
        Button manageArticlesButton = new Button("Manage Articles");
        Button logoutButton = new Button("Logout");

        manageArticlesButton.setOnAction(e -> System.out.println("Navigating to manage articles."));
        logoutButton.setOnAction(e -> stage.setScene(new LoginScreen().getLoginScene(stage)));

        VBox layout = new VBox(15, manageArticlesButton, logoutButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        return new Scene(layout, 400, 300);
    }
}
