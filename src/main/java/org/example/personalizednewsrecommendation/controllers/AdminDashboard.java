package org.example.personalizednewsrecommendation.controllers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.services.UserManager;

public class AdminDashboard {
    private final UserManager userManager;
    private final ArticleManager articleManager;

    public AdminDashboard(UserManager userManager, ArticleManager articleManager) {
        this.userManager = userManager;
        this.articleManager = articleManager;
    }

    public Scene getDashboardScene(Stage stage) {
        Button viewArticlesButton = new Button("View Articles");
        Button addArticleButton = new Button("Add Article");
        Button updateArticleButton = new Button("Update Article");
        Button deleteArticleButton = new Button("Delete Article");
        Button logoutButton = new Button("Logout");

        viewArticlesButton.setOnAction(e -> stage.setScene(new AdminArticleView(userManager, articleManager).getViewScene(stage)));
        addArticleButton.setOnAction(e -> stage.setScene(new AdminAddArticle(userManager, articleManager).getAddArticleScene(stage)));
        updateArticleButton.setOnAction(e -> stage.setScene(new AdminUpdateArticle(userManager, articleManager).getUpdateArticleScene(stage)));
        deleteArticleButton.setOnAction(e -> stage.setScene(new AdminDeleteArticle(userManager, articleManager).getDeleteArticleScene(stage)));
        logoutButton.setOnAction(e -> stage.setScene(new LoginScreen(userManager, articleManager).getLoginScene(stage)));

        VBox layout = new VBox(10, viewArticlesButton, addArticleButton, updateArticleButton, deleteArticleButton, logoutButton);
        return new Scene(layout, 400, 400);
    }
}
