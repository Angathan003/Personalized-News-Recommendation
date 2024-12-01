package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.UserManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class RegistrationScreen {
    private final UserManager userManager;

    public RegistrationScreen(UserManager userManager) {
        this.userManager = userManager;
    }

    public Scene getRegistrationScene(Stage stage) {
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (!username.isEmpty() && !password.isEmpty()) {
                if (userManager.registerUser(username, password)) {
                    Alerts.showSuccess("Registration successful! Please login.");
                    stage.setScene(new LoginScreen(userManager, null).getLoginScene(stage));
                } else {
                    Alerts.showError("Username already exists.");
                }
            } else {
                Alerts.showError("All fields are required!");
            }
        });

        backButton.setOnAction(e -> stage.setScene(new LoginScreen(userManager, null).getLoginScene(stage)));

        VBox layout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, registerButton, backButton);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 400, 300);
    }
}
