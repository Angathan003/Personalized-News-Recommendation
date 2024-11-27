package org.example.personalizednewsrecommendation.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ArticleDisplay {

    private final String username; // Store username locally

    public ArticleDisplay(String username) {
        this.username = username; // Initialize with the username
    }

    public Scene getArticleScene(Stage stage) {
        ListView<String> articleList = new ListView<>();
        ObservableList<String> articles = FXCollections.observableArrayList(
                "Article 1: Technology - AI Advancements",
                "Article 2: Health - Benefits of Meditation",
                "Article 3: Sports - World Cup Updates"
        );
        articleList.setItems(articles);

        Button likeButton = new Button("Like");
        Button skipButton = new Button("Skip");
        Button backButton = new Button("Back");

        likeButton.setOnAction(e -> System.out.println("Article liked by " + username));
        skipButton.setOnAction(e -> System.out.println("Article skipped by " + username));

        // Pass username when going back to HomeDashboard
        backButton.setOnAction(e -> stage.setScene(new HomeDashboard(username).getHomeScene(stage)));

        VBox layout = new VBox(10, articleList, likeButton, skipButton, backButton);
        return new Scene(layout, 600, 400);
    }
}
