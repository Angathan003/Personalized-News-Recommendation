package org.example.personalizednewsrecommendation;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendation.controllers.LoginScreen;
import org.example.personalizednewsrecommendation.services.ArticleManager;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Path to the dataset (make sure this matches your file structure)
        String datasetPath = "src/main/resources/org/example/personalizednewsrecommendation/CNN_Articels_clean.csv";

        // Initialize the ArticleManager with the dataset
        ArticleManager articleManager = new ArticleManager(datasetPath);

        // Set up the initial screen (LoginScreen)
        LoginScreen loginScreen = new LoginScreen(articleManager);
        primaryStage.setScene(loginScreen.getLoginScene(primaryStage));
        primaryStage.setTitle("Personalized News Recommendation System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
