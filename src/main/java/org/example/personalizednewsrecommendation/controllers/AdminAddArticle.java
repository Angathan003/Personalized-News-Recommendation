package org.example.personalizednewsrecommendation.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.services.ArticleManager;
import org.example.personalizednewsrecommendation.services.UserManager;
import org.example.personalizednewsrecommendation.utils.Alerts;

public class AdminAddArticle {
    private final UserManager userManager;
    private final ArticleManager articleManager;

    public AdminAddArticle(UserManager userManager, ArticleManager articleManager) {
        this.userManager = userManager;
        this.articleManager = articleManager;
    }

    public Scene getAddArticleScene(Stage stage) {
        // Labels and text fields for article attributes
        Label authorLabel = new Label("Author:");
        TextField authorField = new TextField();

        Label datePublishedLabel = new Label("Date Published:");
        TextField datePublishedField = new TextField();

        Label categoryLabel = new Label("Category:");
        TextField categoryField = new TextField();

        Label sectionLabel = new Label("Section:");
        TextField sectionField = new TextField();

        Label urlLabel = new Label("URL:");
        TextField urlField = new TextField();

        Label headlineLabel = new Label("Headline:");
        TextField headlineField = new TextField();

        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField();

        Label keywordsLabel = new Label("Keywords:");
        TextField keywordsField = new TextField();

        Label secondHeadlineLabel = new Label("Second Headline:");
        TextField secondHeadlineField = new TextField();

        Label articleTextLabel = new Label("Article Text:");
        TextField articleTextField = new TextField();

        // Buttons
        Button saveButton = new Button("Save");
        Button backButton = new Button("Back");

        // Save button action
        saveButton.setOnAction(e -> {
            String author = authorField.getText();
            String datePublished = datePublishedField.getText();
            String category = categoryField.getText();
            String section = sectionField.getText();
            String url = urlField.getText();
            String headline = headlineField.getText();
            String description = descriptionField.getText();
            String keywords = keywordsField.getText();
            String secondHeadline = secondHeadlineField.getText();
            String articleText = articleTextField.getText();

            if (!author.isEmpty() && !headline.isEmpty() && !articleText.isEmpty()) {
                articleManager.addArticle(
                        "AutoGeneratedIndex", // Example index (you can modify this as needed)
                        author,
                        datePublished,
                        category,
                        section,
                        url,
                        headline,
                        description,
                        keywords,
                        secondHeadline,
                        articleText
                );
                Alerts.showSuccess("Article added successfully!");
                stage.setScene(new AdminDashboard(userManager, articleManager).getDashboardScene(stage));
            } else {
                Alerts.showError("Author, Headline, and Article Text are required fields!");
            }
        });

        // Back button action
        backButton.setOnAction(e -> stage.setScene(new AdminDashboard(userManager, articleManager).getDashboardScene(stage)));

        // Layout
        VBox layout = new VBox(10,
                authorLabel, authorField,
                datePublishedLabel, datePublishedField,
                categoryLabel, categoryField,
                sectionLabel, sectionField,
                urlLabel, urlField,
                headlineLabel, headlineField,
                descriptionLabel, descriptionField,
                keywordsLabel, keywordsField,
                secondHeadlineLabel, secondHeadlineField,
                articleTextLabel, articleTextField,
                saveButton, backButton
        );
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 600, 800); // Adjusted size for better UI
    }
}
