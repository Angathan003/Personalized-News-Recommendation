package org.example.personalizednewsrecommendation.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.services.UserManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class LoginScreen {
    private final UserManager userManager;
    private final ArticleManager articleManager;

    public LoginScreen(UserManager userManager, ArticleManager articleManager) {
        this.userManager = userManager;
        this.articleManager = articleManager;
    }

    public Scene getLoginScene(Stage stage) {
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label userTypeLabel = new Label("Login as:");

        ObservableList<String> userTypes = FXCollections.observableArrayList("Admin", "User");
        ComboBox<String> userTypeComboBox = new ComboBox<>(userTypes);
        userTypeComboBox.setPromptText("Select User Type");

        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String selectedUserType = userTypeComboBox.getValue();

            if (selectedUserType == null) {
                Alerts.showError("Please select a user type!");
                return;
            }

            if ("Admin".equalsIgnoreCase(selectedUserType)) {
                if (userManager.authenticateAdmin(username, password)) {
                    stage.setScene(new AdminDashboard(userManager, articleManager).getDashboardScene(stage));
                } else {
                    Alerts.showError("Invalid admin credentials!");
                }
            } else if ("User".equalsIgnoreCase(selectedUserType)) {
                if (userManager.authenticateUser(username, password)) {
                    // Pass the valid articleManager instance
                    stage.setScene(new UserDashboard(userManager, articleManager, username).getDashboardScene(stage));
                } else {
                    Alerts.showError("Invalid user credentials!");
                }
            }
        });

        GridPane layout = new GridPane();
        layout.setVgap(10);
        layout.setHgap(10);

        layout.add(usernameLabel, 0, 0);
        layout.add(usernameField, 1, 0);
        layout.add(passwordLabel, 0, 1);
        layout.add(passwordField, 1, 1);
        layout.add(userTypeLabel, 0, 2);
        layout.add(userTypeComboBox, 1, 2);
        layout.add(loginButton, 1, 3);

        return new Scene(layout, 400, 300);
    }
}
