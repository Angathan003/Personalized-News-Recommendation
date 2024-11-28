package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.services.UserManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class LoginScreen {

    private final UserManager userManager = new UserManager();
    private final ArticleManager articleManager;

    public LoginScreen(ArticleManager articleManager) {
        this.articleManager = articleManager;
    }

    public Scene getLoginScene(Stage stage) {
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label roleLabel = new Label("Role:");
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("User", "Admin");
        roleDropdown.setValue("User");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        roleDropdown.setOnAction(e -> registerButton.setDisable(roleDropdown.getValue().equals("Admin")));

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleDropdown.getValue();

            if (role.equals("Admin")) {
                if (username.equals("admin") && password.equals("admin123")) {
                    Alerts.showSuccess("Welcome, Admin!");
                    stage.setScene(new AdminDashboard(articleManager).getAdminScene(stage));
                } else {
                    Alerts.showError("Invalid Admin credentials!");
                }
            } else {
                if (userManager.login(username, password)) {
                    Alerts.showSuccess("Welcome, " + username + "!");
                    stage.setScene(new HomeDashboard(username, articleManager).getHomeScene(stage));
                } else {
                    Alerts.showError("Invalid User credentials!");
                }
            }
        });

        registerButton.setOnAction(e -> Alerts.showSuccess("Redirecting to registration..."));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(roleLabel, 0, 2);
        grid.add(roleDropdown, 1, 2);
        grid.add(loginButton, 0, 3);
        grid.add(registerButton, 1, 3);

        return new Scene(grid, 400, 300);
    }
}
