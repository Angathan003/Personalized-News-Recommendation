package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;

public class AdminDashboard {

    private final ArticleManager articleManager;

    public AdminDashboard(ArticleManager articleManager) {
        this.articleManager = articleManager;
    }

    public Scene getAdminScene(Stage stage) {
        Button viewArticlesButton = new Button("View All Articles");
        Button addArticleButton = new Button("Add New Article");
        Button updateArticleButton = new Button("Update Article");
        Button deleteArticleButton = new Button("Delete Article");
        Button logoutButton = new Button("Logout");

        viewArticlesButton.setOnAction(e ->
                stage.setScene(new AdminArticleView(articleManager).getArticleViewScene(stage))
        );
        addArticleButton.setOnAction(e ->
                stage.setScene(new AdminAddArticle(articleManager).getAddArticleScene(stage))
        );
        updateArticleButton.setOnAction(e ->
                stage.setScene(new AdminUpdateArticle(articleManager).getUpdateArticleScene(stage))
        );
        deleteArticleButton.setOnAction(e ->
                stage.setScene(new AdminDeleteArticle(articleManager).getDeleteArticleScene(stage))
        );
        logoutButton.setOnAction(e ->
                stage.setScene(new LoginScreen(articleManager).getLoginScene(stage))
        );

        VBox layout = new VBox(15, viewArticlesButton, addArticleButton, updateArticleButton, deleteArticleButton, logoutButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        return new Scene(layout, 400, 400);
    }
}
