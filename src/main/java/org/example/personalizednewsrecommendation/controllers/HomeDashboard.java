package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;

public class HomeDashboard {

    private final String username;
    private final ArticleManager articleManager;

    public HomeDashboard(String username, ArticleManager articleManager) {
        this.username = username;
        this.articleManager = articleManager;
    }

    public Scene getHomeScene(Stage stage) {
        Button viewArticlesButton = new Button("View Recommended Articles");
        Button settingsButton = new Button("Settings");
        Button logoutButton = new Button("Logout");

        // View Articles Button Action
        viewArticlesButton.setOnAction(e ->
                stage.setScene(new ArticleDisplay(username, articleManager).getArticleScene(stage))
        );

        // Settings Button Action
        settingsButton.setOnAction(e ->
                stage.setScene(new SettingsScreen(username, articleManager).getSettingsScene(stage))
        );

        // Logout Button Action
        logoutButton.setOnAction(e ->
                stage.setScene(new LoginScreen(articleManager).getLoginScene(stage))
        );

        VBox layout = new VBox(15, viewArticlesButton, settingsButton, logoutButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        return new Scene(layout, 400, 300);
    }
}
