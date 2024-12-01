package org.example.personalizednewsrecommendation.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.models.Article;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.services.UserManager;

public class ArticleDisplay {
    private final ArticleManager articleManager;
    private final UserManager userManager;
    private final String username;

    public ArticleDisplay(UserManager userManager, ArticleManager articleManager, String username) {
        this.articleManager = articleManager;
        this.userManager = userManager;
        this.username = username;
    }

    public Scene getArticleScene(Stage stage) {
        // Title Label
        Label titleLabel = new Label("Available Articles");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Article List
        ListView<String> articleListView = new ListView<>();
        ObservableList<String> articleTitles = FXCollections.observableArrayList();
        for (Article article : articleManager.getArticles()) {
            articleTitles.add(article.getHeadline());
        }
        articleListView.setItems(articleTitles);

        // Article Content Display
        TextArea articleContentArea = new TextArea();
        articleContentArea.setEditable(false);
        articleContentArea.setWrapText(true);
        articleContentArea.setStyle("-fx-control-inner-background: #f4f4f4; -fx-font-size: 14px;");

        // Buttons
        Button viewButton = new Button("View Content");
        Button backButton = new Button("Back");

        // Layouts
        HBox buttonBox = new HBox(10, viewButton, backButton);
        buttonBox.setStyle("-fx-padding: 10px;");
        buttonBox.setSpacing(15);

        VBox leftPane = new VBox(10, titleLabel, articleListView);
        leftPane.setStyle("-fx-padding: 10px; -fx-border-color: #cccccc; -fx-border-width: 1px;");
        leftPane.setSpacing(10);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(leftPane);
        mainLayout.setCenter(articleContentArea);
        mainLayout.setBottom(buttonBox);

        // Styling
        mainLayout.setStyle("-fx-padding: 20px; -fx-background-color: #ffffff;");

        // Event Handlers
        viewButton.setOnAction(e -> {
            String selectedTitle = articleListView.getSelectionModel().getSelectedItem();
            if (selectedTitle != null) {
                Article selectedArticle = articleManager.getArticleByTitle(selectedTitle);
                if (selectedArticle != null) {
                    articleContentArea.setText(selectedArticle.getArticleText());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Article not found.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select an article to view.");
            }
        });

        backButton.setOnAction(e -> stage.setScene(new HomeDashboard(userManager, articleManager, username).getHomeScene(stage)));

        return new Scene(mainLayout, 800, 600);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
